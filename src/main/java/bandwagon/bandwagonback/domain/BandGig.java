package bandwagon.bandwagonback.domain;

import bandwagon.bandwagonback.dto.UserPerformanceDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "band_gigs")
@Getter @Setter
public class BandGig {
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

    public BandGig() {}

    public BandGig(UserPerformanceDto userPerformanceDto) {
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
