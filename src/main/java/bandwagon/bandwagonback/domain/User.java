package bandwagon.bandwagonback.domain;

import bandwagon.bandwagonback.dto.SignUpRequest;
import lombok.Getter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "users")
@Getter
public class User {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    private String name;
    private String nickname;
    private String email;
    private String password;
    private Boolean gender; // 0 == Male, 1 == Female
    private Date birthday;

    public User() {
    }
    public User(SignUpRequest request) {
        this.name = request.getName();
        this.nickname = request.getNickname();
        this.email = request.getEmail();
        this.password = request.getPassword();
        this.gender = request.getGender();
        this.birthday = request.getBirthday();
    }
}
