package bandwagon.bandwagonback.domain;

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
}
