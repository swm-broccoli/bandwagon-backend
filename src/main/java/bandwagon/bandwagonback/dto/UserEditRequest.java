package bandwagon.bandwagonback.dto;

import lombok.Data;

import java.util.Date;

@Data
public class UserEditRequest {
    private String name;
    private String nickname;
    private String email;
    private Boolean gender; // 0 == Male, 1 == Female
    private Date birthday;
}
