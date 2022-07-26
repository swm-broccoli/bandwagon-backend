package bandwagon.bandwagonback.dto;

import bandwagon.bandwagonback.domain.BandGig;
import bandwagon.bandwagonback.domain.BandPractice;
import bandwagon.bandwagonback.domain.UserPerformance;
import lombok.Data;
import java.util.Date;

@Data
public class PerformanceDto {

    private Long id;

    private String musicTitle;
    private Date performDate;
    private String videoUrl;
    private String audioUrl;

    public PerformanceDto() {}

    public PerformanceDto(UserPerformance userPerformance){
        this.id = userPerformance.getId();
        this.musicTitle = userPerformance.getMusicTitle();
        this.performDate = userPerformance.getPerformDate();
        this.videoUrl = userPerformance.getVideoUrl();
        this.audioUrl = userPerformance.getAudioUrl();
    }
    public PerformanceDto(BandGig bandGig){
        this.id = bandGig.getId();
        this.musicTitle = bandGig.getMusicTitle();
        this.performDate = bandGig.getPerformDate();
        this.videoUrl = bandGig.getVideoUrl();
        this.audioUrl = bandGig.getAudioUrl();
    }
    public PerformanceDto(BandPractice bandPractice){
        this.id = bandPractice.getId();
        this.musicTitle = bandPractice.getMusicTitle();
        this.performDate = bandPractice.getPerformDate();
        this.videoUrl = bandPractice.getVideoUrl();
        this.audioUrl = bandPractice.getAudioUrl();
    }

}
