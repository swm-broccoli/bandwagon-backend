package bandwagon.bandwagonback.dto.subdto;

import bandwagon.bandwagonback.domain.Band;
import bandwagon.bandwagonback.domain.User;
import lombok.Data;

@Data
public class BasicInfoDto {

    private Long id;
    private String name;
    private String avatarUrl;

    public BasicInfoDto(Long id, String name, String avatarUrl) {
        this.id = id;
        this.name = name;
        this.avatarUrl = avatarUrl;
    }

    public BasicInfoDto(User user) {
        this.id = user.getId();
        this.name = user.getNickname();
        this.avatarUrl = user.getUserInfo().getAvatarUrl();
    }

    public BasicInfoDto(Band band) {
        this.id = band.getId();
        this.name = band.getName();
        this.avatarUrl = band.getAvatarUrl();
    }
}
