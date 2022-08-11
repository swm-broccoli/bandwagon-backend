package bandwagon.bandwagonback.listener;

import bandwagon.bandwagonback.domain.Band;
import bandwagon.bandwagonback.service.S3Uploader;
import lombok.RequiredArgsConstructor;

import javax.persistence.PreRemove;
import java.net.URISyntaxException;

@RequiredArgsConstructor
public class BandEntityListener {

    private final S3Uploader s3Uploader;

    @PreRemove
    public void preRemove(Band band) throws URISyntaxException {
        s3Uploader.deleteFromS3(band.getAvatarUrl());
    }
}
