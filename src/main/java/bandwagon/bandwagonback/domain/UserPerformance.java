package bandwagon.bandwagonback.domain;

import bandwagon.bandwagonback.dto.UserPerformanceDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "user_performances")
@Getter @Setter
public class UserPerformance {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String musicTitle;
    private Date performDate;

    @Column(columnDefinition="TEXT")
    private String videoUrl;

    @Column(columnDefinition="TEXT")
    private String audioUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public UserPerformance() {}

    public UserPerformance(UserPerformanceDto userPerformanceDto) {
        this.musicTitle = userPerformanceDto.getMusicTitle();
        this.performDate = userPerformanceDto.getPerformDate();
        this.videoUrl = userPerformanceDto.getVideoUrl();
        this.audioUrl = userPerformanceDto.getAudioUrl();
    }

    public void update(UserPerformanceDto userPerformanceDto) {
        this.musicTitle = userPerformanceDto.getMusicTitle();
        this.performDate = userPerformanceDto.getPerformDate();
        this.videoUrl = userPerformanceDto.getVideoUrl();
        this.audioUrl = userPerformanceDto.getAudioUrl();
    }
}
