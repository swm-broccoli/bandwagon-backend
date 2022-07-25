package bandwagon.bandwagonback.domain;

import bandwagon.bandwagonback.dto.PerformanceDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "band_practices")
@Getter @Setter
public class BandPractice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String musicTitle;
    private Date performDate;

    @Column(columnDefinition="TEXT")
    private String videoUrl;

    @Column(columnDefinition="TEXT")
    private String audioUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "band_id")
    private Band band;

    public BandPractice() {}

    public BandPractice(PerformanceDto performanceDto) {
        this.musicTitle = performanceDto.getMusicTitle();
        this.performDate = performanceDto.getPerformDate();
        this.videoUrl = performanceDto.getVideoUrl();
        this.audioUrl = performanceDto.getAudioUrl();
    }

    public void update(PerformanceDto performanceDto) {
        this.musicTitle = performanceDto.getMusicTitle();
        this.performDate = performanceDto.getPerformDate();
        this.videoUrl = performanceDto.getVideoUrl();
        this.audioUrl = performanceDto.getAudioUrl();
    }
}
