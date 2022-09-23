package bandwagon.bandwagonback.dto;

import lombok.Data;

@Data
public class UserTokenDto {
    private String email;

    private Boolean isSocial;

    public UserTokenDto(String email, Boolean isSocial) {
        this.email = email;
        this.isSocial = isSocial;
    }
}
