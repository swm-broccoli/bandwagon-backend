package bandwagon.bandwagonback.service;

import bandwagon.bandwagonback.domain.Band;
import bandwagon.bandwagonback.domain.Day;
import bandwagon.bandwagonback.domain.Genre;
import bandwagon.bandwagonback.dto.exception.notfound.BandNotFoundException;
import bandwagon.bandwagonback.dto.exception.notfound.DayNotFoundException;
import bandwagon.bandwagonback.dto.exception.notof.UserNotOfBandException;
import bandwagon.bandwagonback.repository.BandMemberRepository;
import bandwagon.bandwagonback.repository.BandRepository;
import bandwagon.bandwagonback.repository.DayRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DayService {

    private final DayRepository dayRepository;
    private final BandRepository bandRepository;
    private final BandMemberRepository bandMemberRepository;

    @Transactional
    public void addDayToBand(String email, Long bandId, Long dayId) {
        Band band = bandRepository.findById(bandId).orElse(null);
        if (band == null) {
            throw new BandNotFoundException();
        }
        if (bandMemberRepository.findFirstByMember_emailAndBand_id(email, bandId) == null) {
            throw new UserNotOfBandException();
        }
        Day day = dayRepository.findById(dayId).orElse(null);
        if (day == null) {
            throw new DayNotFoundException();
        }
        if (band.getDays().contains(day)) {
            log.info("User already has day: {}", day.getDay());
            return;
        }
        band.addDay(day);
    }

    @Transactional
    public void deleteDayFromBand(String email, Long bandId, Long dayId) {
        Band band = bandRepository.findById(bandId).orElse(null);
        if (band == null) {
            throw new BandNotFoundException();
        }
        if (bandMemberRepository.findFirstByMember_emailAndBand_id(email, bandId) == null) {
            throw new UserNotOfBandException();
        }
        Day day = dayRepository.findById(dayId).orElse(null);
        if (day == null) {
            throw new DayNotFoundException();
        }
        band.removeDay(day);
    }
}
