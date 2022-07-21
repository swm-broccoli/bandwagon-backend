package bandwagon.bandwagonback.domain;

import bandwagon.bandwagonback.dto.OAuthAttributes;
import bandwagon.bandwagonback.dto.SignUpRequest;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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

    private Boolean isSocial;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<UserPerformance> userPerformances = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user")
    private UserInfo userInfo;

    @ManyToMany
    @JoinTable(name = "user_positions",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "position_id"))
    private List<Position> positions = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "user_genres",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private List<Genre> genres = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "user_areas",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "area_id"))
    private List<Area> areas = new ArrayList<>();

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
        this.isSocial = false;
    }

    //OAuth 로그인
    public User(OAuthAttributes attributes) {
        this.name = attributes.getName();
        this.email = attributes.getEmail();

        this.nickname = "TempUser" + UUID.randomUUID();
        this.gender = false;
        this.birthday = new Date();
        this.isSocial = true;
    }

    // 연주기록 추가
    public void addUserPerformance(UserPerformance userPerformance) {
        this.userPerformances.add(userPerformance);
        userPerformance.setUser(this);
    }

    // 포지션 추가
    public void addPosition(Position position) {
        this.positions.add(position);
        position.getUsers().add(this);
    }

    // 포지션 제거
    public void removePosition(Position position) {
        this.positions.remove(position);
        position.getUsers().remove(this);
    }

    // 선호 장르 추가
    public void addGenre(Genre genre) {
        this.genres.add(genre);
        genre.getUsers().add(this);
    }

    // 선호 장르 제거
    public void removeGenre(Genre genre) {
        this.genres.remove(genre);
        genre.getUsers().remove(this);
    }

    // 활동 지역 추가
    public void addArea(Area area) {
        this.areas.add(area);
        area.getUsers().add(this);
    }

    // 활동 지역 제거
    public void removeArea(Area area) {
        this.areas.remove(area);
        area.getUsers().remove(this);
    }
}
