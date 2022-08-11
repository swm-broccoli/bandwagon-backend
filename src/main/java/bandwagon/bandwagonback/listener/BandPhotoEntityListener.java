package bandwagon.bandwagonback.listener;

import bandwagon.bandwagonback.domain.BandPhoto;
import bandwagon.bandwagonback.service.S3Uploader;
import lombok.RequiredArgsConstructor;

import javax.persistence.PreRemove;
import java.net.URISyntaxException;

@RequiredArgsConstructor
public class BandPhotoEntityListener {

    private final S3Uploader s3Uploader;

    @PreRemove
    public void preRemove(BandPhoto bandPhoto) throws URISyntaxException {
        s3Uploader.deleteFromS3(bandPhoto.getImgUrl());
    }
}
