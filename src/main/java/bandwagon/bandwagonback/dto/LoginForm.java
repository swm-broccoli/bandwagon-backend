package bandwagon.bandwagonback.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LoginForm {
    @Schema(description = "유저 이메일", example = "test@test.com")
    private String email;
    @Schema(description = "비밀번호", example = "0000")
    private String password;
}
