package bandwagon.bandwagonback.dto;

import lombok.Data;

import java.util.Date;

@Data
public class LoginResponse {
    private String accessToken;

    private String refreshToken;
    private String nickname;
    private Date expiresAt;

    public LoginResponse(String accessToken, String refreshToken, String nickname, Date expiresAt) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.nickname = nickname;
        this.expiresAt = expiresAt;
    }
}
