package bandwagon.bandwagonback.service;

import bandwagon.bandwagonback.domain.Band;
import bandwagon.bandwagonback.domain.BandGig;
import bandwagon.bandwagonback.dto.PerformanceDto;
import bandwagon.bandwagonback.dto.exception.notfound.BandGigNotFoundException;
import bandwagon.bandwagonback.dto.exception.notfound.BandNotFoundException;
import bandwagon.bandwagonback.dto.exception.notof.BandGigNotOfBandException;
import bandwagon.bandwagonback.dto.exception.notof.UserNotOfBandException;
import bandwagon.bandwagonback.repository.BandGigRepository;
import bandwagon.bandwagonback.repository.BandMemberRepository;
import bandwagon.bandwagonback.repository.BandRepository;
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
    public void saveBandGig(Long bandId, String email, PerformanceDto performanceDto) {
        Band band = confirmUserInBand(email, bandId);
        BandGig bandGig = new BandGig(performanceDto);
        band.addBandGig(bandGig);
        bandGigRepository.save(bandGig);
    }

    // 기존 공연기록 삭제
    @Transactional
    public void deleteBandGig(Long bandId, String email, Long bandGigId) {
        Band band = confirmUserInBand(email, bandId);
        BandGig bandGig = bandGigRepository.findById(bandGigId).orElse(null);
        if (bandGig == null) {
            throw new BandGigNotFoundException();
        }
        if(bandGig.getBand() != band) {
            throw new BandGigNotOfBandException();
        }
        bandGigRepository.deleteById(bandGigId);
    }

    // 기존 공연기록 수정
    @Transactional
    public void updateBandGig(Long bandId, String email, Long bandGigId, PerformanceDto performanceDto) {
        Band band = confirmUserInBand(email, bandId);
        BandGig bandGig = bandGigRepository.findById(bandGigId).orElse(null);
        if (bandGig == null) {
            throw new BandGigNotFoundException();
        }
        if(bandGig.getBand() != band) {
            throw new BandGigNotOfBandException();
        }
        bandGig.update(performanceDto);
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
