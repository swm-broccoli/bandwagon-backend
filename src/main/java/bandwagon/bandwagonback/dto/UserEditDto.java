package bandwagon.bandwagonback.dto;

import bandwagon.bandwagonback.domain.User;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class UserEditDto {
    private final String name;
    private final String nickname;
    private final String email;
    private final Boolean gender;
    private final LocalDate birthday;
    public UserEditDto(User user) {
        this.name = user.getName();
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.gender = user.getGender();
        this.birthday = user.getBirthday();
    }
}
