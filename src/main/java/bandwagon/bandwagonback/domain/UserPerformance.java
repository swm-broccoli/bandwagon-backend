package bandwagon.bandwagonback.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "user_performances")
@Getter @Setter
public class UserPerformance {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_performance_id")
    private Long id;

    private String musicTitle;
    private Date performDate;

    @Column(columnDefinition="TEXT")
    private String video_url;
    @Column(columnDefinition="TEXT")
    private String audio_url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}
