package bandwagon.bandwagonback.listener;

import bandwagon.bandwagonback.domain.UserInfo;
import bandwagon.bandwagonback.service.S3Uploader;
import lombok.RequiredArgsConstructor;

import javax.persistence.PreRemove;
import java.net.URISyntaxException;

@RequiredArgsConstructor
public class UserInfoEntityListener {

    private final S3Uploader s3Uploader;

    @PreRemove
    public void preRemove(UserInfo userInfo) throws URISyntaxException {
        s3Uploader.deleteFromS3(userInfo.getAvatarUrl());
    }
}
