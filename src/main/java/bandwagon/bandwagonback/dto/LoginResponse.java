package bandwagon.bandwagonback.dto;

import lombok.Data;

@Data
public class LoginResponse {
    private String jwt;
    private String nickname;

    public LoginResponse(String jwt, String nickname) {
        this.jwt = jwt;
        this.nickname = nickname;
    }
}
