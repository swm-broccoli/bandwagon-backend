package bandwagon.bandwagonback.service;

import bandwagon.bandwagonback.domain.Band;
import bandwagon.bandwagonback.domain.BandMember;
import bandwagon.bandwagonback.domain.User;
import bandwagon.bandwagonback.dto.BandCreateForm;
import bandwagon.bandwagonback.dto.BandPageDto;
import bandwagon.bandwagonback.dto.exception.NoBandException;
import bandwagon.bandwagonback.repository.BandMemberRepository;
import bandwagon.bandwagonback.repository.BandRepository;
import bandwagon.bandwagonback.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BandService {

    private final BandRepository bandRepository;
    private final UserRepository userRepository;
    private final BandMemberRepository bandMemberRepository;
    private final S3Uploader s3Uploader;
    private final BandMemberService bandMemberService;

    /**
     * 로그인 된 유저의 밴드 페이지 조회
     */
    public BandPageDto getUsersBandPage(String email) throws Exception {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            throw new Exception("존재하지 않는 유저입니다!");
        }
        BandMember bandMember = user.getBandMember();
        if (bandMember == null) {
            throw new NoBandException("가입된 밴드가 없습니다.");
        }
        return new BandPageDto(bandMember.getBand(), bandMember.getIsFrontman());
    }

    /**
     * 특정 밴드의 밴드 페이지 조회 (for band post)
     */
    public BandPageDto getOtherBandPage(Long bandId) throws Exception {
        Band band = bandRepository.findById(bandId).orElse(null);
        if (band == null) {
            throw new Exception("존재하지 않는 밴드입니다!");
        }
        return new BandPageDto(band, false);
    }

    /**
     * 밴드 생성
     */
    @Transactional
    public Long createBand(String email, BandCreateForm bandCreateForm) throws Exception {
        User user = userRepository.findByEmail(email).orElse(null);
        if(user == null) {
            throw new Exception("존재하지 않는 유저입니다!");
        }
        if(user.getBandMember() != null) {
            throw new Exception("밴드에 이미 가입한 유저입니다!");
        }
        Band band = new Band(bandCreateForm);
        bandRepository.save(band);
        BandMember bandMember = new BandMember(user, true);
        band.addBandMember(bandMember);
        bandMemberRepository.save(bandMember);
        return band.getId();
    }

    /**
     * 밴드 삭제
     */
    @Transactional
    public void disbandBand(String email, Long bandId) throws Exception {
        bandMemberService.confirmUserIsFrontman(email, bandId);
        bandRepository.deleteById(bandId);
    }

    /**
     * 밴드 이름 변경
     */
    @Transactional
    public void editName(String email, Long bandId, String newName) throws Exception {
        Band band = confirmUserInBand(email, bandId);
        band.setName(newName);
    }

    /**
     * 밴드 소개 변경
     */
    @Transactional
    public void editDescription(String email, Long bandId, String newDescription) throws Exception {
        Band band = confirmUserInBand(email, bandId);
        band.setDescription(newDescription);
    }

    /**
     * 밴드 아바타 변경
     */
    @Transactional
    public String uploadAvatar(String email, Long bandId, MultipartFile multipartFile) throws Exception {
        Band band = confirmUserInBand(email, bandId);
        if (band.getAvatarUrl() != null && band.getAvatarUrl().length() != 0) {
            s3Uploader.deleteFromS3(band.getAvatarUrl().replace(File.separatorChar, '/'));
        }
        String imgUrl = s3Uploader.upload(multipartFile, "band/avatar");
        band.setAvatarUrl(imgUrl);
        return imgUrl;
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
