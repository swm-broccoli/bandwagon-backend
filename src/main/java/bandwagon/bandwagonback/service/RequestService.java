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
    public void declineApplyRequest(String email, Long requestId) throws Exception {
        Request request = requestRepository.findById(requestId).orElse(null);
        if (request == null || !request.getType().equals(RequestType.APPLY)) {
            throw new Exception("Specified Request is Not Valid");
        }
        BandMember bandMember = bandMemberRepository.findFirstByMember_emailAndBand_id(email, request.getBand().getId());
        if (bandMember == null) {
            throw new Exception("Declining User is not Part of the Band");
        }
        if (!bandMember.getIsFrontman()) {
            throw new Exception("Declining User is not frontman and cannot decline this Request!");
        }
        requestRepository.delete(request);
    }
}
