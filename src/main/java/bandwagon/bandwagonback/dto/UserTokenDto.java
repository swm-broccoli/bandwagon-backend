package bandwagon.bandwagonback.dto;

import lombok.Data;

@Data
public class UserTokenDto {
    private String email;

    public UserTokenDto(String email) {
        this.email = email;
    }
}
