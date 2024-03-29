package bandwagon.bandwagonback.service;

import bandwagon.bandwagonback.domain.*;
import bandwagon.bandwagonback.domain.post.BandPost;
import bandwagon.bandwagonback.dto.RequestDto;
import bandwagon.bandwagonback.dto.RequestListDto;
import bandwagon.bandwagonback.dto.exception.DuplicateRequestException;
import bandwagon.bandwagonback.dto.exception.inband.UserInBandException;
import bandwagon.bandwagonback.dto.exception.notauthorized.UserNotAuthorizedException;
import bandwagon.bandwagonback.dto.exception.notauthorized.UserNotFrontmanException;
import bandwagon.bandwagonback.dto.exception.notfound.PostNotFoundException;
import bandwagon.bandwagonback.dto.exception.notfound.RequestNotFoundException;
import bandwagon.bandwagonback.dto.exception.notof.UserNotOfBandException;
import bandwagon.bandwagonback.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RequestService {

    private final NotificationService notificationService;
    private final RequestRepository requestRepository;
    private final BandPostRepository bandPostRepository;
    private final BandMemberRepository bandMemberRepository;

    public RequestListDto getRequestOnUser(User user, RequestType type) {
        List<Request> invitesToUser = requestRepository.findAllByUserAndType(user, type);
        List<RequestDto> requestDtos = invitesToUser.stream().map(RequestDto::new).collect(Collectors.toList());
        return new RequestListDto(requestDtos);
    }

    public RequestListDto getRequestOnBand(Band band, RequestType type) {
        List<Request> invitesFromBand = requestRepository.findAllByBandAndType(band, type);
        List<RequestDto> requestDtos = invitesFromBand.stream().map(RequestDto::new).collect(Collectors.toList());
        return new RequestListDto(requestDtos);
    }

    @Transactional
    public void createRequest(User user, Band band, RequestType requestType, BandPost bandPost) {
        Request request = new Request();
        request.setType(requestType);
        user.addRequest(request);
        band.addRequest(request);
        if (requestType.equals(RequestType.APPLY)) {
            bandPost.addRequest(request);
        }
        requestRepository.save(request);
    }

    @Transactional
    public void sendInviteRequest(User invitingUser, User invitedUser) {
        if (!invitingUser.getBandMember().getIsFrontman()) {
            throw new UserNotFrontmanException();
        }
        Band invitingBand = invitingUser.getBandMember().getBand();
        if (invitedUser.getBandMember() != null) {
            throw new UserInBandException();
        }
        if (requestRepository.existsByUserAndBandAndType(invitedUser, invitingBand, RequestType.INVITE)) {
            throw new DuplicateRequestException();
        }
        createRequest(invitedUser, invitingBand, RequestType.INVITE, null);
        notificationService.createBandToUser(invitingBand, invitedUser, NotificationType.INVITE);
    }

    @Transactional
    public void sendApplyRequest(User applyingUser, Long postId) {
        if (applyingUser.getBandMember() != null) {
            throw new UserInBandException();
        }
        BandPost bandPost = bandPostRepository.findById(postId).orElse(null);
        if (bandPost == null) {
            throw new PostNotFoundException();
        }
        Band appliedBand = bandPost.getBand();
        if (requestRepository.existsByUserAndBandPostAndType(applyingUser, bandPost, RequestType.APPLY)) {
            throw new DuplicateRequestException();
        }
        createRequest(applyingUser, appliedBand, RequestType.APPLY, bandPost);
        notificationService.createUserToBand(applyingUser, appliedBand, NotificationType.APPLY);
    }

    @Transactional
    public void acceptApplyRequest(User user, Long requestId) {
        Request request = requestRepository.findById(requestId).orElse(null);
        if (request == null || !request.getType().equals(RequestType.APPLY)) {
            throw new RequestNotFoundException();
        }
        BandMember bandMember = bandMemberRepository.findByMemberAndBand(user, request.getBand()).orElse(null);
        if (bandMember == null) {
            throw new UserNotOfBandException();
        }
        if (!bandMember.getIsFrontman()) {
            throw new UserNotFrontmanException();
        }
        // Accepting logic
        User candidateUser = request.getUser();
        if (candidateUser.getBandMember() != null) {
            throw new UserInBandException();
        }
        BandMember newMember = new BandMember(candidateUser, false);
        request.getBand().addBandMember(newMember);
        bandMemberRepository.save(newMember);
        requestRepository.delete(request);
        notificationService.createBandToUser(request.getBand(), candidateUser, NotificationType.APPLY_ACCEPT);
    }

    @Transactional
    public void declineApplyRequest(User user, Long requestId) {
        Request request = requestRepository.findById(requestId).orElse(null);
        if (request == null || !request.getType().equals(RequestType.APPLY)) {
            throw new RequestNotFoundException();
        }
        BandMember bandMember = bandMemberRepository.findByMemberAndBand(user, request.getBand()).orElse(null);
        if (bandMember == null) {
            throw new UserNotOfBandException();
        }
        if (!bandMember.getIsFrontman()) {
            throw new UserNotFrontmanException();
        }
        requestRepository.delete(request);
        notificationService.createBandToUser(request.getBand(), request.getUser(), NotificationType.APPLY_DECLINE);
    }

    @Transactional
    public void cancelApplyRequest(User user, Long requestId) {
        Request request = requestRepository.findById(requestId).orElse(null);
        if (request == null || !request.getType().equals(RequestType.APPLY)) {
            throw new RequestNotFoundException();
        }
        if (!user.equals(request.getUser())) {
            throw new UserNotAuthorizedException();
        }
        requestRepository.delete(request);
    }

    @Transactional
    public void acceptInviteRequest(User user, Long requestId) {
        Request request = requestRepository.findById(requestId).orElse(null);
        if (request == null || !request.getType().equals(RequestType.INVITE)) {
            throw new RequestNotFoundException();
        }
        if (!user.equals(request.getUser())) {
            throw new UserNotAuthorizedException();
        }
        //Accepting logic
        if (user.getBandMember() != null) {
            throw new UserInBandException();
        }
        // 자기 자신은 알림 받지 않게 밴드에 추가 되기 직전에 밴드 맴버들에게 알림 발송
        notificationService.createUserToBand(user, request.getBand(), NotificationType.INVITE_ACCEPT);
        BandMember newMember = new BandMember(user, false);
        request.getBand().addBandMember(newMember);
        bandMemberRepository.save(newMember);
        requestRepository.delete(request);
    }

    @Transactional
    public void declineInviteRequest(User user, Long requestId) {
        Request request = requestRepository.findById(requestId).orElse(null);
        if (request == null || !request.getType().equals(RequestType.INVITE)) {
            throw new RequestNotFoundException();
        }
        if (!user.equals(request.getUser())) {
            throw new UserNotAuthorizedException();
        }
        requestRepository.delete(request);
        notificationService.createUserToBand(user, request.getBand(), NotificationType.INVITE_DECLINE);
    }

    @Transactional
    public void cancelInviteRequest(User user, Long requestId) {
        Request request = requestRepository.findById(requestId).orElse(null);
        if (request == null || !request.getType().equals(RequestType.INVITE)) {
            throw new RequestNotFoundException();
        }
        BandMember bandMember = bandMemberRepository.findByMemberAndBand(user, request.getBand()).orElse(null);
        if (bandMember == null) {
            throw new UserNotOfBandException();
        }
        if (!bandMember.getIsFrontman()) {
            throw new UserNotFrontmanException();
        }
        requestRepository.delete(request);
    }
}
