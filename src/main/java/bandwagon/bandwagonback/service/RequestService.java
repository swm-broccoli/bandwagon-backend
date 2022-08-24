package bandwagon.bandwagonback.service;

import bandwagon.bandwagonback.domain.*;
import bandwagon.bandwagonback.domain.post.BandPost;
import bandwagon.bandwagonback.dto.RequestDto;
import bandwagon.bandwagonback.dto.RequestListDto;
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

    public RequestListDto getInviteToUser(User user) {
        List<Request> invitesToUser = requestRepository.findAllByUserAndType(user, RequestType.INVITE);
        List<RequestDto> requestDtos = invitesToUser.stream().map(RequestDto::new).collect(Collectors.toList());
        return new RequestListDto(requestDtos);
    }

    public RequestListDto getInviteFromBand(Band band) {
        List<Request> invitesFromBand = requestRepository.findAllByBandAndType(band, RequestType.INVITE);
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
    public void sendInviteRequest(User invitingUser, User invitedUser) throws Exception {
        if (!invitingUser.getBandMember().getIsFrontman()) {
            throw new Exception("Inviting User is not frontman!");
        }
        Band invitingBand = invitingUser.getBandMember().getBand();
        if (invitedUser.getBandMember() != null) {
            throw new Exception("Invited User is already in band!");
        }
        // 중복 검사 로직?
        createRequest(invitedUser, invitingBand, RequestType.INVITE, null);
        notificationService.createBandToUser(invitingBand, invitedUser, NotificationType.INVITE);
    }

    @Transactional
    public void sendApplyRequest(User applyingUser, Long postId) throws Exception {
        if (applyingUser.getBandMember() != null) {
            throw new Exception("Applying user is already in band!");
        }
        BandPost bandPost = bandPostRepository.findById(postId).orElse(null);
        if (bandPost == null) {
            throw new Exception("Can't find Post User is applying to");
        }
        Band appliedBand = bandPost.getBand();
        // 중복 검사 로직
        createRequest(applyingUser, appliedBand, RequestType.APPLY, bandPost);
        notificationService.createUserToBand(applyingUser, appliedBand, NotificationType.APPLY);
    }

    @Transactional
    public void acceptApplyRequest(User user, Long requestId) throws Exception {
        Request request = requestRepository.findById(requestId).orElse(null);
        if (request == null || !request.getType().equals(RequestType.APPLY)) {
            throw new Exception("Specified Request is Not Valid");
        }
        BandMember bandMember = bandMemberRepository.findByMemberAndBand(user, request.getBand()).orElse(null);
        if (bandMember == null) {
            throw new Exception("Accepting User is not Part of the Band in request");
        }
        if (!bandMember.getIsFrontman()) {
            throw new Exception("Accepting User is not frontman and cannot decline this Request!");
        }
        // Accepting logic
        User candidateUser = request.getUser();
        if (candidateUser.getBandMember() != null) {
            throw new Exception("이미 밴드에 속한 유저입니다");
        }
        BandMember newMember = new BandMember(candidateUser, false);
        request.getBand().addBandMember(newMember);
        bandMemberRepository.save(newMember);
        requestRepository.delete(request);
        notificationService.createBandToUser(request.getBand(), candidateUser, NotificationType.APPLY_ACCEPT);
    }

    @Transactional
    public void declineApplyRequest(User user, Long requestId) throws Exception {
        Request request = requestRepository.findById(requestId).orElse(null);
        if (request == null || !request.getType().equals(RequestType.APPLY)) {
            throw new Exception("Specified Request is Not Valid");
        }
        BandMember bandMember = bandMemberRepository.findByMemberAndBand(user, request.getBand()).orElse(null);
        if (bandMember == null) {
            throw new Exception("Declining User is not Part of the Band in request");
        }
        if (!bandMember.getIsFrontman()) {
            throw new Exception("Declining User is not frontman and cannot decline this Request!");
        }
        requestRepository.delete(request);
        notificationService.createBandToUser(request.getBand(), request.getUser(), NotificationType.APPLY_DECLINE);
    }

    @Transactional
    public void cancelApplyRequest(User user, Long requestId) throws Exception {
        Request request = requestRepository.findById(requestId).orElse(null);
        if (request == null || !request.getType().equals(RequestType.APPLY)) {
            throw new Exception("Specified Request is Not Valid");
        }
        if (!user.equals(request.getUser())) {
            throw new Exception("User is not owner of Apply Request");
        }
        requestRepository.delete(request);
    }

    @Transactional
    public void acceptInviteRequest(User user, Long requestId) throws Exception {
        Request request = requestRepository.findById(requestId).orElse(null);
        if (request == null || !request.getType().equals(RequestType.INVITE)) {
            throw new Exception("Specified Request is Not Valid");
        }
        if (!user.equals(request.getUser())) {
            throw new Exception("Accepting user is not User in request!");
        }
        //Accepting logic
        if (user.getBandMember() != null) {
            throw new Exception("이미 밴드에 속한 유저입니다");
        }
        // 자기 자신은 알림 받지 않게 밴드에 추가 되기 직전에 밴드 맴버들에게 알림 발송
        notificationService.createUserToBand(user, request.getBand(), NotificationType.INVITE_ACCEPT);
        BandMember newMember = new BandMember(user, false);
        request.getBand().addBandMember(newMember);
        bandMemberRepository.save(newMember);
        requestRepository.delete(request);
    }

    @Transactional
    public void declineInviteRequest(User user, Long requestId) throws Exception {
        Request request = requestRepository.findById(requestId).orElse(null);
        if (request == null || !request.getType().equals(RequestType.INVITE)) {
            throw new Exception("Specified Request is Not Valid");
        }
        if (!user.equals(request.getUser())) {
            throw new Exception("Declining user is not User in request!");
        }
        requestRepository.delete(request);
        notificationService.createUserToBand(user, request.getBand(), NotificationType.INVITE_DECLINE);
    }

    @Transactional
    public void cancelInviteRequest(User user, Long requestId) throws Exception {
        Request request = requestRepository.findById(requestId).orElse(null);
        if (request == null || !request.getType().equals(RequestType.INVITE)) {
            throw new Exception("Specified Request is Not Valid");
        }
        BandMember bandMember = bandMemberRepository.findByMemberAndBand(user, request.getBand()).orElse(null);
        if (bandMember == null) {
            throw new Exception("Canceling User is not Part of the Band in request");
        }
        if (!bandMember.getIsFrontman()) {
            throw new Exception("Canceling User is not frontman and cannot decline this Request!");
        }
        requestRepository.delete(request);
    }
}
