package bandwagon.bandwagonback.dto;

import bandwagon.bandwagonback.domain.Band;
import bandwagon.bandwagonback.domain.User;
import lombok.Data;

@Data
public class RandomPageDto {

    private Long id;
    private String avatarUrl;
    private String userEmail;
    private String name;
    private String description;
    private String dtype;

    public RandomPageDto(User user) {
        this.id = user.getId();
        this.avatarUrl = user.getUserInfo().getAvatarUrl();
        this.userEmail = user.getEmail();
        this.name = user.getName();
        this.description = user.getUserInfo().getDescription();
        this.dtype = "User";
    }

    public RandomPageDto(Band band) {
        this.id = band.getId();
        this.avatarUrl = band.getAvatarUrl();
        this.userEmail = null;
        this.name = band.getName();
        this.description = band.getDescription();
        this.dtype = "Band";
    }
}
