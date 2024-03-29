package bandwagon.bandwagonback.domain;

import bandwagon.bandwagonback.listener.UserInfoEntityListener;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "user_infos")
@Getter @Setter
@EntityListeners(UserInfoEntityListener.class)
public class UserInfo {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_info_id")
    private Long Id;

    @Column(columnDefinition="TEXT")
    private String description;

    @Column(columnDefinition="TEXT")
    private String avatarUrl;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
