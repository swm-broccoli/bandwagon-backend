package bandwagon.bandwagonback.domain;

import bandwagon.bandwagonback.dto.OAuthAttributes;
import bandwagon.bandwagonback.dto.SignUpRequest;
import bandwagon.bandwagonback.dto.UserEditRequest;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;

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
    private LocalDate birthday;

    private Boolean isSocial;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<UserPerformance> userPerformances = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user")
    private UserInfo userInfo;

    @ManyToMany
    @JoinTable(name = "user_positions",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "position_id"))
    private Set<Position> positions = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "user_genres",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private Set<Genre> genres = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "user_areas",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "area_id"))
    private Set<Area> areas = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "member")
    private BandMember bandMember;

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
        this.birthday = LocalDate.now();
        this.isSocial = true;
    }

    // User 기본 정보 수정
    public void updateUser(UserEditRequest request) {
        if(request.getName() != null && !request.getName().equals("")) {
            this.name = request.getName();
        }
        if(request.getNickname() != null && !request.getNickname().equals("")) {
            this.nickname = request.getNickname();
        }
        if(request.getGender() != null) {
            this.gender = request.getGender();
        }
        if(request.getBirthday() != null) {
            this.birthday = request.getBirthday();
        }
    }

    // 나이 추출
    public int getUserAge() {
        return LocalDate.now().getYear() - this.birthday.getYear() + 1;
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
