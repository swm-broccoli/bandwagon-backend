package bandwagon.bandwagonback.dto;

import lombok.Data;

@Data
public class PasswordEditRequest {
    private String oldPassword;
    private String newPassword;
    private String newPasswordCheck;
}
