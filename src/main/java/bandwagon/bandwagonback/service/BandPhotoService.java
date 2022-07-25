package bandwagon.bandwagonback.service;

import bandwagon.bandwagonback.domain.Band;
import bandwagon.bandwagonback.domain.BandPhoto;
import bandwagon.bandwagonback.repository.BandMemberRepository;
import bandwagon.bandwagonback.repository.BandPhotoRepository;
import bandwagon.bandwagonback.repository.BandRepository;
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
public class BandPhotoService {

    private final BandPhotoRepository bandPhotoRepository;
    private final BandRepository bandRepository;
    private final BandMemberRepository bandMemberRepository;
    private final S3Uploader s3Uploader;

    @Transactional
    public String addPhotoToBand(String email, Long bandId, MultipartFile multipartFile) throws Exception {
        Band band = confirmUserInBand(email, bandId);
        String imgUrl = s3Uploader.upload(multipartFile, "band/photo");
        BandPhoto bandPhoto = new BandPhoto(imgUrl);
        band.addBandPhoto(bandPhoto);
        bandPhotoRepository.save(bandPhoto);
        return imgUrl;
    }

    @Transactional
    public void removePhotoFromBand(String email, Long bandId, Long bandPhotoId) throws Exception {
        Band band = confirmUserInBand(email, bandId);
        BandPhoto bandPhoto = bandPhotoRepository.findById(bandPhotoId).orElse(null);
        if (bandPhoto.getBand() != band) {
            throw new Exception("해당 밴드의 사진이 아닙니다!");
        }
        s3Uploader.deleteFromS3(bandPhoto.getImgUrl().replace(File.separatorChar, '/'));
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
