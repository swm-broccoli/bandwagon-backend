package bandwagon.bandwagonback.dto;

import lombok.Data;

import java.util.Date;

@Data
public class UserEditRequest {
    private String name;
    private String nickname;
    private Boolean gender; // 0 == Male, 1 == Female
    private Date birthday;

    private String oldPassword;
    private String newPassword;
    private String newPasswordCheck;
}
