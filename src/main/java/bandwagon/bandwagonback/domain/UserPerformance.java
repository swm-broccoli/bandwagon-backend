package bandwagon.bandwagonback.domain;

import lombok.Getter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "user_performances")
@Getter
public class UserPerformance {

    @Id @GeneratedValue
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
