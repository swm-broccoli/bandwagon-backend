package bandwagon.bandwagonback.service;

import bandwagon.bandwagonback.domain.Band;
import bandwagon.bandwagonback.domain.BandMember;
import bandwagon.bandwagonback.domain.NotificationType;
import bandwagon.bandwagonback.domain.User;
import bandwagon.bandwagonback.dto.BandCreateForm;
import bandwagon.bandwagonback.dto.BandPageDto;
import bandwagon.bandwagonback.dto.exception.inband.UserInBandException;
import bandwagon.bandwagonback.dto.exception.inband.UserNotInBandException;
import bandwagon.bandwagonback.dto.exception.notfound.BandNotFoundException;
import bandwagon.bandwagonback.dto.exception.notfound.UserNotFoundException;
import bandwagon.bandwagonback.dto.exception.notof.UserNotOfBandException;
import bandwagon.bandwagonback.repository.BandMemberRepository;
import bandwagon.bandwagonback.repository.BandRepository;
import bandwagon.bandwagonback.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BandService {

    private final NotificationService notificationService;
    private final BandRepository bandRepository;
    private final UserRepository userRepository;
    private final BandMemberRepository bandMemberRepository;
    private final S3Uploader s3Uploader;
    private final BandMemberService bandMemberService;

    /**
     * 유저의 밴드 반환
     */
    public Band getUsersBand(User user) {
        BandMember bandMember = user.getBandMember();
        if (bandMember == null) {
            throw new UserNotInBandException();
        }
        Band band = bandMember.getBand();
        if (band == null) {
            throw new BandNotFoundException();
        }
        return band;
    }

    /**
     * 로그인 된 유저의 밴드 페이지 조회
     */
    public BandPageDto getUsersBandPage(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            throw new UserNotFoundException();
        }
        BandMember bandMember = user.getBandMember();
        if (bandMember == null) {
            throw new UserNotInBandException();
        }
        return new BandPageDto(bandMember.getBand(), bandMember.getIsFrontman());
    }

    /**
     * 특정 밴드의 밴드 페이지 조회 (for band post)
     */
    public BandPageDto getOtherBandPage(Long bandId) {
        Band band = bandRepository.findById(bandId).orElse(null);
        if (band == null) {
            throw new BandNotFoundException();
        }
        return new BandPageDto(band, false);
    }

    /**
     * 밴드 생성
     */
    @Transactional
    public Long createBand(String email, BandCreateForm bandCreateForm) {
        User user = userRepository.findByEmail(email).orElse(null);
        if(user == null) {
            throw new UserNotFoundException();
        }
        if(user.getBandMember() != null) {
            throw new UserInBandException();
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
    public void disbandBand(String email, Long bandId) {
        bandMemberService.confirmUserIsFrontman(email, bandId);
        User user = userRepository.findByEmail(email).orElse(null);
        Band band = bandRepository.findById(bandId).orElse(null);
        // 둘 중 하나가 null이면 confirmIsFrontman에서 exception 터지므로 따로 검증 X
        notificationService.createUserToBand(user, band, NotificationType.DISBAND);
        bandRepository.deleteById(bandId);
    }

    /**
     * 밴드 이름 변경
     */
    @Transactional
    public void editName(String email, Long bandId, String newName) {
        Band band = confirmUserInBand(email, bandId);
        band.setName(newName);
    }

    /**
     * 밴드 소개 변경
     */
    @Transactional
    public void editDescription(String email, Long bandId, String newDescription) {
        Band band = confirmUserInBand(email, bandId);
        band.setDescription(newDescription);
    }

    /**
     * 밴드 아바타 변경
     */
    @Transactional
    public String uploadAvatar(String email, Long bandId, MultipartFile multipartFile) throws URISyntaxException, IOException {
        Band band = confirmUserInBand(email, bandId);
        if (band.getAvatarUrl() != null && band.getAvatarUrl().length() != 0) {
            s3Uploader.deleteFromS3(band.getAvatarUrl().replace(File.separatorChar, '/'));
        }
        String imgUrl = s3Uploader.upload(multipartFile, "band/avatar");
        band.setAvatarUrl(imgUrl);
        return imgUrl;
    }

    /**
     * 총 밴드 수 조회
     */
    private long getBandCount() {
        return bandRepository.count();
    }

    /**
     * 랜덤 밴드 조히
     */
    public Band getRandomBand() {
        long bandCount = getBandCount();
        int randomIndex = (int) (Math.random() * bandCount);
        Page<Band> randomBand = bandRepository.findAll(PageRequest.of(randomIndex, 1));
        return randomBand.getContent().get(0);
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
