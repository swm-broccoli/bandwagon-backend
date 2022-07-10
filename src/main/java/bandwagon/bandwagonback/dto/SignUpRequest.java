package bandwagon.bandwagonback.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
public class SignUpRequest {
    @Schema(description = "이름", example = "편장욱")
    private String name;
    @Schema(description = "닉네임", example = "소마")
    private String nickname;
    @Schema(description = "이메일", example = "pyun0825@naver.com")
    private String email;
    @Schema(description = "비밀번호", example = "1111")
    private String password;
    @Schema(description = "비밀번호 중복체크", example = "1111")
    private String passwordCheck;
    @Schema(description = "성별", example = "false")
    private Boolean gender; // 0 == Male, 1 == Female
    @Schema(description = "생일", example = "1998-08-25")
    private Date birthday;
}
