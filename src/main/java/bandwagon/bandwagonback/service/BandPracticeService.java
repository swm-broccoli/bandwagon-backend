package bandwagon.bandwagonback.service;

import bandwagon.bandwagonback.domain.Band;
import bandwagon.bandwagonback.domain.BandPractice;
import bandwagon.bandwagonback.dto.PerformanceDto;
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
    public void saveBandPractice(Long bandId, String email, PerformanceDto performanceDto) throws Exception {
        Band band = bandRepository.findById(bandId).orElse(null);
        if (band == null) {
            throw new Exception("존재하지 않는 밴드입니다!");
        }
        if (bandMemberRepository.findFirstByMember_emailAndBand_id(email, bandId) == null) {
            throw new Exception("해당 밴드에 속하지 않은 유저입니다!");
        }
        BandPractice bandPractice = new BandPractice(performanceDto);
        band.addBandPractice(bandPractice);
        bandPracticeRepository.save(bandPractice);
    }

    // 기존 연습기록 삭제
    @Transactional
    public void deleteBandGig(Long bandId, String email, Long bandPracticeId) throws Exception {
        Band band = bandRepository.findById(bandId).orElse(null);
        if (band == null) {
            throw new Exception("존재하지 않는 밴드입니다!");
        }
        if (bandMemberRepository.findFirstByMember_emailAndBand_id(email, bandId) == null) {
            throw new Exception("해당 밴드에 속하지 않은 유저입니다!");
        }
        BandPractice bandPractice = bandPracticeRepository.findById(bandPracticeId).orElse(null);
        if (bandPractice == null) {
            throw new Exception("존재하지 않는 연주기록입니다!");
        }
        if(bandPractice.getBand() != band) {
            throw new Exception("해당 밴드의 연주기록이 아닙니다!");
        }
        bandPracticeRepository.deleteById(bandPracticeId);
    }

    // 기존 연습기록 수정
    @Transactional
    public void updateBandGig(Long bandId, String email, Long bandPracticeId, PerformanceDto performanceDto) throws Exception {
        Band band = bandRepository.findById(bandId).orElse(null);
        if (band == null) {
            throw new Exception("존재하지 않는 밴드입니다!");
        }
        if (bandMemberRepository.findFirstByMember_emailAndBand_id(email, bandId) == null) {
            throw new Exception("해당 밴드에 속하지 않은 유저입니다!");
        }
        BandPractice bandPractice = bandPracticeRepository.findById(bandPracticeId).orElse(null);
        if (bandPractice == null) {
            throw new Exception("존재하지 않는 연주기록입니다!");
        }
        if(bandPractice.getBand() != band) {
            throw new Exception("해당 밴드의 연주기록이 아닙니다!");
        }
        bandPractice.update(performanceDto);
    }
}
