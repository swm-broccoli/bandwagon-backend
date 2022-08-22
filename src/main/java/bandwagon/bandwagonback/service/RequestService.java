package bandwagon.bandwagonback.service;

import bandwagon.bandwagonback.domain.*;
import bandwagon.bandwagonback.domain.post.BandPost;
import bandwagon.bandwagonback.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RequestService {

    private final RequestRepository requestRepository;
    private final BandPostRepository bandPostRepository;
    private final UserRepository userRepository;
    private final BandMemberRepository bandMemberRepository;

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
    }

    @Transactional
    public void acceptApplyRequest(String email, Long requestId) throws Exception {
        Request request = requestRepository.findById(requestId).orElse(null);
        if (request == null || !request.getType().equals(RequestType.APPLY)) {
            throw new Exception("Specified Request is Not Valid");
        }
        BandMember bandMember = bandMemberRepository.findFirstByMember_emailAndBand_id(email, request.getBand().getId());
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
    }

    @Transactional
    public void declineApplyRequest(String email, Long requestId) throws Exception {
        Request request = requestRepository.findById(requestId).orElse(null);
        if (request == null || !request.getType().equals(RequestType.APPLY)) {
            throw new Exception("Specified Request is Not Valid");
        }
        BandMember bandMember = bandMemberRepository.findFirstByMember_emailAndBand_id(email, request.getBand().getId());
        if (bandMember == null) {
            throw new Exception("Declining User is not Part of the Band in request");
        }
        if (!bandMember.getIsFrontman()) {
            throw new Exception("Declining User is not frontman and cannot decline this Request!");
        }
        requestRepository.delete(request);
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
