package bandwagon.bandwagonback.service;

import bandwagon.bandwagonback.domain.Band;
import bandwagon.bandwagonback.domain.BandMember;
import bandwagon.bandwagonback.domain.User;
import bandwagon.bandwagonback.dto.BandCreateForm;
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
     * 밴드 이름 변경
     */
    @Transactional
    public void editName(String email, Long bandId, String newName) throws Exception {
        Band band = bandRepository.findById(bandId).orElse(null);
        if(band == null) {
            throw new Exception("존재하지 않는 밴드입니다!");
        }
        User user = userRepository.findByEmail(email).orElse(null);
        if(user == null) {
            throw new Exception("존재하지 않는 유저입니다!");
        }
        BandMember bandMember = bandMemberRepository.findFirstByMemberAndBand(user, band);
        if (bandMember == null) {
            throw new Exception("해당 밴드에 속하지 않은 유저입니다!");
        }
        band.setName(newName);
    }

    /**
     * 밴드 소개 변경
     */
    @Transactional
    public void editDescription(String email, Long bandId, String newDescription) throws Exception {
        Band band = bandRepository.findById(bandId).orElse(null);
        if(band == null) {
            throw new Exception("존재하지 않는 밴드입니다!");
        }
        User user = userRepository.findByEmail(email).orElse(null);
        if(user == null) {
            throw new Exception("존재하지 않는 유저입니다!");
        }
        BandMember bandMember = bandMemberRepository.findFirstByMemberAndBand(user, band);
        if (bandMember == null) {
            throw new Exception("해당 밴드에 속하지 않은 유저입니다!");
        }
        band.setDescription(newDescription);
    }

    /**
     * 밴드 아바타 변경
     */
    @Transactional
    public String uploadAvatar(String email, Long bandId, MultipartFile multipartFile) throws Exception {
        Band band = bandRepository.findById(bandId).orElse(null);
        if(band == null) {
            throw new Exception("존재하지 않는 밴드입니다!");
        }
        User user = userRepository.findByEmail(email).orElse(null);
        if(user == null) {
            throw new Exception("존재하지 않는 유저입니다!");
        }
        BandMember bandMember = bandMemberRepository.findFirstByMemberAndBand(user, band);
        if (bandMember == null) {
            throw new Exception("해당 밴드에 속하지 않은 유저입니다!");
        }
        if (band.getAvatarUrl() != null) {
            s3Uploader.deleteFromS3(band.getAvatarUrl().replace(File.separatorChar, '/'));
        }
        String imgUrl = s3Uploader.upload(multipartFile, "band/avatar");
        band.setAvatarUrl(imgUrl);
        return imgUrl;
    }
}
