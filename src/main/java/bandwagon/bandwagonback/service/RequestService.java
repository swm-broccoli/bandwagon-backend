package bandwagon.bandwagonback.service;

import bandwagon.bandwagonback.domain.*;
import bandwagon.bandwagonback.repository.BandMemberRepository;
import bandwagon.bandwagonback.repository.RequestRepository;
import bandwagon.bandwagonback.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final BandMemberRepository bandMemberRepository;

    @Transactional
    public void createRequest(User user, Band band, RequestType requestType) {
        Request request = new Request();
        request.setType(requestType);
        user.addRequest(request);
        band.addRequest(request);
        requestRepository.save(request);
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
    public void cancelApplyRequest(String email, Long requestId) throws Exception {
        Request request = requestRepository.findById(requestId).orElse(null);
        if (request == null || !request.getType().equals(RequestType.APPLY)) {
            throw new Exception("Specified Request is Not Valid");
        }
        if (!email.equals(request.getUser().getEmail())) {
            throw new Exception("User is not owner of Apply Request");
        }
        requestRepository.delete(request);
    }

    @Transactional
    public void acceptInviteRequest(String email, Long requestId) throws Exception {
        Request request = requestRepository.findById(requestId).orElse(null);
        if (request == null || !request.getType().equals(RequestType.INVITE)) {
            throw new Exception("Specified Request is Not Valid");
        }
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            throw new Exception("User does not exist!");
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
    public void declineInviteRequest(String email, Long requestId) throws Exception {
        Request request = requestRepository.findById(requestId).orElse(null);
        if (request == null || !request.getType().equals(RequestType.INVITE)) {
            throw new Exception("Specified Request is Not Valid");
        }
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            throw new Exception("User does not exist!");
        }
        if (!user.equals(request.getUser())) {
            throw new Exception("Declining user is not User in request!");
        }
        requestRepository.delete(request);
    }

    @Transactional
    public void cancelInviteRequest(String email, Long requestId) throws Exception {
        Request request = requestRepository.findById(requestId).orElse(null);
        if (request == null || !request.getType().equals(RequestType.INVITE)) {
            throw new Exception("Specified Request is Not Valid");
        }
        BandMember bandMember = bandMemberRepository.findFirstByMember_emailAndBand_id(email, request.getBand().getId());
        if (bandMember == null) {
            throw new Exception("Canceling User is not Part of the Band in request");
        }
        if (!bandMember.getIsFrontman()) {
            throw new Exception("Canceling User is not frontman and cannot decline this Request!");
        }
        requestRepository.delete(request);
    }
}
