package bandwagon.bandwagonback.domain;

import bandwagon.bandwagonback.dto.MyPageDto;
import bandwagon.bandwagonback.dto.OAuthAttributes;
import bandwagon.bandwagonback.dto.SignUpRequest;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
@Getter @Setter
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String name;
    private String nickname;
    private String email;
    private String password;
    private Boolean gender; // 0 == Male, 1 == Female
    private Date birthday;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user")
    private UserInfo userInfo;

    public User() {
    }
    
    // 일반 로그인
    public User(SignUpRequest request) {
        this.name = request.getName();
        this.nickname = request.getNickname();
        this.email = request.getEmail();
        this.password = request.getPassword();
        this.gender = request.getGender();
        this.birthday = request.getBirthday();
    }

    //OAuth 로그인
    public User(OAuthAttributes attributes) {
        this.name = attributes.getName();
        this.email = attributes.getEmail();

        this.nickname = "TempUser";
        this.gender = false;
        this.birthday = new Date();
    }

    // 마이 페이지 변경으로인한 유저 정보 변경
    public void myPageUpdate(MyPageDto myPageDto) {
        this.name = myPageDto.getName();
        this.birthday = myPageDto.getBirthday();
        this.userInfo.setPosition(myPageDto.getPosition());
        this.userInfo.setArea(myPageDto.getArea());
        this.userInfo.setGenre(myPageDto.getGenre());
        this.userInfo.setDescription(myPageDto.getDescription());
        this.userInfo.setUserPerformances(myPageDto.getUserPerformances());
    }
}
