package bandwagon.bandwagonback.service;

import bandwagon.bandwagonback.domain.Band;
import bandwagon.bandwagonback.domain.Day;
import bandwagon.bandwagonback.domain.Genre;
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
    public void addDayToBand(String email, Long bandId, Long dayId) throws Exception {
        Band band = bandRepository.findById(bandId).orElse(null);
        if (band == null) {
            throw new Exception("Band does not exist!");
        }
        if (bandMemberRepository.findFirstByMember_emailAndBand_id(email, bandId) == null) {
            throw new Exception("해당 밴드에 속하지 않은 유저입니다!");
        }
        Day day = dayRepository.findById(dayId).orElse(null);
        if (day == null) {
            throw new Exception("Day does not exist!");
        }
        if (band.getDays().contains(day)) {
            log.info("User already has day: {}", day.getDay());
            return;
        }
        band.addDay(day);
    }

    @Transactional
    public void deleteDayFromBand(String email, Long bandId, Long dayId) throws Exception {
        Band band = bandRepository.findById(bandId).orElse(null);
        if (band == null) {
            throw new Exception("Band does not exist!");
        }
        if (bandMemberRepository.findFirstByMember_emailAndBand_id(email, bandId) == null) {
            throw new Exception("해당 밴드에 속하지 않은 유저입니다!");
        }
        Day day = dayRepository.findById(dayId).orElse(null);
        if (day == null) {
            throw new Exception("Day does not exist!");
        }
        band.removeDay(day);
    }
}
