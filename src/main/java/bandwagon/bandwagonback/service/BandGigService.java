package bandwagon.bandwagonback.service;

import bandwagon.bandwagonback.domain.Band;
import bandwagon.bandwagonback.domain.BandGig;
import bandwagon.bandwagonback.domain.BandMember;
import bandwagon.bandwagonback.domain.User;
import bandwagon.bandwagonback.dto.PerformanceDto;
import bandwagon.bandwagonback.repository.BandGigRepository;
import bandwagon.bandwagonback.repository.BandMemberRepository;
import bandwagon.bandwagonback.repository.BandRepository;
import bandwagon.bandwagonback.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BandGigService {

    private final BandGigRepository bandGigRepository;
    private final BandMemberRepository bandMemberRepository;
    private final BandRepository bandRepository;

    // 신규 공연기록 저장
    @Transactional
    public void saveBandGig(Long bandId, String email, PerformanceDto performanceDto) throws Exception {
        Band band = confirmUserInBand(email, bandId);
        BandGig bandGig = new BandGig(performanceDto);
        band.addBandGig(bandGig);
        bandGigRepository.save(bandGig);
    }

    // 기존 공연기록 삭제
    @Transactional
    public void deleteBandGig(Long bandId, String email, Long bandGigId) throws Exception {
        Band band = confirmUserInBand(email, bandId);
        BandGig bandGig = bandGigRepository.findById(bandGigId).orElse(null);
        if (bandGig == null) {
            throw new Exception("존재하지 않는 공연기록입니다!");
        }
        if(bandGig.getBand() != band) {
            throw new Exception("해당 밴드의 공연기록이 아닙니다!");
        }
        bandGigRepository.deleteById(bandGigId);
    }

    // 기존 공연기록 수정
    @Transactional
    public void updateBandGig(Long bandId, String email, Long bandGigId, PerformanceDto performanceDto) throws Exception {
        Band band = confirmUserInBand(email, bandId);
        BandGig bandGig = bandGigRepository.findById(bandGigId).orElse(null);
        if (bandGig == null) {
            throw new Exception("존재하지 않는 공연기록입니다!");
        }
        if(bandGig.getBand() != band) {
            throw new Exception("해당 밴드의 공연기록이 아닙니다!");
        }
        bandGig.update(performanceDto);
    }

    @Transactional
    public Band confirmUserInBand(String email, Long bandId) throws Exception {
        Band band = bandRepository.findById(bandId).orElse(null);
        if(band == null) {
            throw new Exception("존재하지 않는 밴드입니다!");
        }
        if (bandMemberRepository.findFirstByMember_emailAndBand_id(email, bandId) == null) {
            throw new Exception("해당 밴드에 속하지 않은 유저입니다!");
        }
        return band;
    }
}
