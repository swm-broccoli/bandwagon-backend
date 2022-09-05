package bandwagon.bandwagonback.service;

import bandwagon.bandwagonback.domain.Band;
import bandwagon.bandwagonback.domain.BandPractice;
import bandwagon.bandwagonback.dto.PerformanceDto;
import bandwagon.bandwagonback.dto.exception.notfound.BandNotFoundException;
import bandwagon.bandwagonback.dto.exception.notfound.BandPracticeNotFoundException;
import bandwagon.bandwagonback.dto.exception.notof.BandPracticeNotOfBandException;
import bandwagon.bandwagonback.dto.exception.notof.UserNotOfBandException;
import bandwagon.bandwagonback.repository.BandMemberRepository;
import bandwagon.bandwagonback.repository.BandPracticeRepository;
import bandwagon.bandwagonback.repository.BandRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BandPracticeService {

    private final BandPracticeRepository bandPracticeRepository;
    private final BandMemberRepository bandMemberRepository;
    private final BandRepository bandRepository;

    // 신규 연습기록 저장
    @Transactional
    public void saveBandPractice(Long bandId, String email, PerformanceDto performanceDto) {
        Band band = confirmUserInBand(email, bandId);
        BandPractice bandPractice = new BandPractice(performanceDto);
        band.addBandPractice(bandPractice);
        bandPracticeRepository.save(bandPractice);
    }

    // 기존 연습기록 삭제
    @Transactional
    public void deleteBandPractice(Long bandId, String email, Long bandPracticeId) {
        Band band = confirmUserInBand(email, bandId);
        BandPractice bandPractice = bandPracticeRepository.findById(bandPracticeId).orElse(null);
        if (bandPractice == null) {
            throw new BandPracticeNotFoundException();
        }
        if(bandPractice.getBand() != band) {
            throw new BandPracticeNotOfBandException();
        }
        bandPracticeRepository.deleteById(bandPracticeId);
    }

    // 기존 연습기록 수정
    @Transactional
    public void updateBandPractice(Long bandId, String email, Long bandPracticeId, PerformanceDto performanceDto) {
        Band band = confirmUserInBand(email, bandId);
        BandPractice bandPractice = bandPracticeRepository.findById(bandPracticeId).orElse(null);
        if (bandPractice == null) {
            throw new BandPracticeNotFoundException();
        }
        if(bandPractice.getBand() != band) {
            throw new BandPracticeNotOfBandException();
        }
        bandPractice.update(performanceDto);
    }

    @Transactional
    public Band confirmUserInBand(String email, Long bandId) {
        Band band = bandRepository.findById(bandId).orElse(null);
        if(band == null) {
            throw new BandNotFoundException();
        }
        if (bandMemberRepository.findFirstByMember_emailAndBand_id(email, bandId) == null) {
            throw new UserNotOfBandException();
        }
        return band;
    }
}
