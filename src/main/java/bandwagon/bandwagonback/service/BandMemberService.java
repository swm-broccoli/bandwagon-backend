package bandwagon.bandwagonback.service;

import bandwagon.bandwagonback.domain.*;
import bandwagon.bandwagonback.repository.BandMemberRepository;
import bandwagon.bandwagonback.repository.BandRepository;
import bandwagon.bandwagonback.repository.PositionRepository;
import bandwagon.bandwagonback.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BandMemberService {

    private final NotificationService notificationService;
    private final BandMemberRepository bandMemberRepository;
    private final BandRepository bandRepository;
    private final UserRepository userRepository;
    private final PositionRepository positionRepository;

    public Long getBandIdByUserEmail(String email) throws Exception {
        BandMember bandMember = bandMemberRepository.findFirstByMember_email(email).orElse(null);
        if (bandMember == null) {
           throw new Exception("밴드에 속한 유저가 아닙니다!");
        }
        return bandMember.getBand().getId();
    }

    @Transactional
    public Long addMemberToBand(String email, Long bandId, String candidateEmail) throws Exception {
        confirmUserIsFrontman(email, bandId);
        User candidateUser = userRepository.findByEmail(candidateEmail).orElse(null);
        if (candidateUser == null) {
            throw new Exception("존재하지 않는 유저입니다!");
        }
        Band band = bandRepository.findById(bandId).orElse(null);
        if (band == null) {
            throw new Exception("존재하지 않는 밴드입니다!");
        }
        if (candidateUser.getBandMember() != null) {
            throw new Exception("이미 밴드에 속한 유저입니다");
        }
        BandMember newMember = new BandMember(candidateUser, false);
        band.addBandMember(newMember);
        bandMemberRepository.save(newMember);
        return newMember.getId();
    }

    @Transactional
    public void removeMemberFromBand(String email, Long bandId, Long bandMemberId) throws Exception {
        confirmUserIsFrontman(email, bandId);
        Band band = bandRepository.findById(bandId).orElse(null);
        BandMember bandMember = bandMemberRepository.findById(bandMemberId).orElse(null);
        if (bandMember == null || !Objects.equals(bandMember.getBand().getId(), bandId)) {
            throw new Exception("해당 밴드에 속하지 않은 유저입니다!");
        }
        if (bandMember.getIsFrontman()) {
            throw new Exception("프런트맨을(자신을) 탈퇴시킬 수 없습니다!");
        }
        User removedMember = bandMember.getMember();
        bandMemberRepository.deleteById(bandMemberId);
        notificationService.createBandToUser(band, removedMember, NotificationType.KICK);
    }

    @Transactional
    public void withdrawMemberFromBand(String email, Long bandId) throws Exception {
        BandMember bandMember = bandMemberRepository.findFirstByMember_emailAndBand_id(email, bandId);
        if (bandMember == null) {
            throw new Exception("해당 밴드에 속하지 않은 유저입니다!");
        }
        if (bandMember.getIsFrontman()) {
            throw new Exception("프런트맨이라 탈퇴하실 수 없습니다!");
        }
        bandMemberRepository.delete(bandMember);

    }

    @Transactional
    public void addPositionToBandMember(String email, Long bandId, Long bandMemberId, Long positionId) throws Exception {
        confirmUserIsFrontman(email, bandId);
        BandMember bandMember = bandMemberRepository.findById(bandMemberId).orElse(null);
        if (bandMember == null || !Objects.equals(bandMember.getBand().getId(), bandId)) {
            throw new Exception("해당 밴드에 속하지 않은 유저입니다!");
        }
        Position position = positionRepository.findById(positionId).orElse(null);
        if (position == null) {
            throw new Exception("Position does not exist!");
        }
        if (bandMember.getPositions().contains(position)){
            log.info("User already has position: {}", position.getPosition());
            return;
        }
        bandMember.addPosition(position);
    }

    @Transactional
    public void deletePositionFromBandMember(String email, Long bandId, Long bandMemberId, Long positionId) throws Exception {
        confirmUserIsFrontman(email, bandId);
        BandMember bandMember = bandMemberRepository.findById(bandMemberId).orElse(null);
        if (bandMember == null || !Objects.equals(bandMember.getBand().getId(), bandId)) {
            throw new Exception("해당 밴드에 속하지 않은 유저입니다!");
        }
        Position position = positionRepository.findById(positionId).orElse(null);
        if (position == null) {
            throw new Exception("Position does not exist!");
        }
        bandMember.removePosition(position);
    }

    @Transactional
    public void changeFrontman(String editerEmail, Long targetBandMemberId, Long bandId) throws Exception {
        BandMember editingMember = confirmUserIsFrontman(editerEmail, bandId);
        BandMember targetMember = bandMemberRepository.findById(targetBandMemberId).orElse(null);
        if (targetMember == null || !Objects.equals(targetMember.getBand().getId(), bandId)) {
            throw new Exception("해당 밴드에 속하지 않은 유저입니다!");
        }
        targetMember.setIsFrontman(true);
        editingMember.setIsFrontman(false);
    }

    @Transactional
    public BandMember confirmUserIsFrontman(String email, Long bandId) throws Exception {
        BandMember member = bandMemberRepository.findFirstByMember_emailAndBand_id(email, bandId);
        if (member == null) {
            throw new Exception("해당 밴드 - 유저 조합이 존재하지 않습니다!");
        }
        if (!member.getIsFrontman()) {
            throw new Exception("프런트맨이 아니라 수정할 수 없습니다!");
        }
        return member;
    }
}
