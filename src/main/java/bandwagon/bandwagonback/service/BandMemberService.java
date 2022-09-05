package bandwagon.bandwagonback.service;

import bandwagon.bandwagonback.domain.*;
import bandwagon.bandwagonback.dto.exception.inband.UserInBandException;
import bandwagon.bandwagonback.dto.exception.inband.UserNotInBandException;
import bandwagon.bandwagonback.dto.exception.notauthorized.FrontmanCannotLeaveException;
import bandwagon.bandwagonback.dto.exception.notfound.BandNotFoundException;
import bandwagon.bandwagonback.dto.exception.notfound.PositionNotFoundException;
import bandwagon.bandwagonback.dto.exception.notfound.UserNotFoundException;
import bandwagon.bandwagonback.dto.exception.notof.UserNotOfBandException;
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

    public Long getBandIdByUserEmail(String email) {
        BandMember bandMember = bandMemberRepository.findFirstByMember_email(email).orElse(null);
        if (bandMember == null) {
           throw new UserNotInBandException();
        }
        return bandMember.getBand().getId();
    }

    @Transactional
    public Long addMemberToBand(String email, Long bandId, String candidateEmail) {
        confirmUserIsFrontman(email, bandId);
        User candidateUser = userRepository.findByEmail(candidateEmail).orElse(null);
        if (candidateUser == null) {
            throw new UserNotFoundException();
        }
        Band band = bandRepository.findById(bandId).orElse(null);
        if (band == null) {
            throw new BandNotFoundException();
        }
        if (candidateUser.getBandMember() != null) {
            throw new UserInBandException();
        }
        BandMember newMember = new BandMember(candidateUser, false);
        band.addBandMember(newMember);
        bandMemberRepository.save(newMember);
        return newMember.getId();
    }

    @Transactional
    public void removeMemberFromBand(String email, Long bandId, Long bandMemberId) {
        confirmUserIsFrontman(email, bandId);
        BandMember bandMember = bandMemberRepository.findById(bandMemberId).orElse(null);
        if (bandMember == null || !Objects.equals(bandMember.getBand().getId(), bandId)) {
            throw new UserNotOfBandException();
        }
        if (bandMember.getIsFrontman()) {
            throw new FrontmanCannotLeaveException();
        }
        Band band = bandMember.getBand();
        User removedMember = bandMember.getMember();
        bandMemberRepository.delete(bandMember);
        band.removeBandMember(bandMember);
        notificationService.createBandToUser(band, removedMember, NotificationType.KICK);
    }

    @Transactional
    public void withdrawMemberFromBand(String email, Long bandId) {
        BandMember bandMember = bandMemberRepository.findFirstByMember_emailAndBand_id(email, bandId);
        if (bandMember == null) {
            throw new UserNotOfBandException();
        }
        if (bandMember.getIsFrontman()) {
            throw new FrontmanCannotLeaveException();
        }
        User withdrawingUser = bandMember.getMember();
        Band band = bandMember.getBand();
        bandMemberRepository.delete(bandMember);
        band.removeBandMember(bandMember);
        notificationService.createUserToBand(withdrawingUser, band, NotificationType.WITHDRAW);
    }

    @Transactional
    public void addPositionToBandMember(String email, Long bandId, Long bandMemberId, Long positionId) {
        confirmUserIsFrontman(email, bandId);
        BandMember bandMember = bandMemberRepository.findById(bandMemberId).orElse(null);
        if (bandMember == null || !Objects.equals(bandMember.getBand().getId(), bandId)) {
            throw new UserNotOfBandException();
        }
        Position position = positionRepository.findById(positionId).orElse(null);
        if (position == null) {
            throw new PositionNotFoundException();
        }
        if (bandMember.getPositions().contains(position)){
            log.info("User already has position: {}", position.getPosition());
            return;
        }
        bandMember.addPosition(position);
    }

    @Transactional
    public void deletePositionFromBandMember(String email, Long bandId, Long bandMemberId, Long positionId) {
        confirmUserIsFrontman(email, bandId);
        BandMember bandMember = bandMemberRepository.findById(bandMemberId).orElse(null);
        if (bandMember == null || !Objects.equals(bandMember.getBand().getId(), bandId)) {
            throw new UserNotOfBandException();
        }
        Position position = positionRepository.findById(positionId).orElse(null);
        if (position == null) {
            throw new PositionNotFoundException();
        }
        bandMember.removePosition(position);
    }

    @Transactional
    public void changeFrontman(String editerEmail, Long targetBandMemberId, Long bandId) {
        BandMember editingMember = confirmUserIsFrontman(editerEmail, bandId);
        BandMember targetMember = bandMemberRepository.findById(targetBandMemberId).orElse(null);
        if (targetMember == null || !Objects.equals(targetMember.getBand().getId(), bandId)) {
            throw new UserNotOfBandException();
        }
        targetMember.setIsFrontman(true);
        editingMember.setIsFrontman(false);
    }

    @Transactional
    public BandMember confirmUserIsFrontman(String email, Long bandId) {
        BandMember member = bandMemberRepository.findFirstByMember_emailAndBand_id(email, bandId);
        if (member == null) {
            throw new UserNotOfBandException();
        }
        if (!member.getIsFrontman()) {
            throw new UserNotFoundException();
        }
        return member;
    }
}
