package bandwagon.bandwagonback.dto;

import bandwagon.bandwagonback.domain.UserPerformance;
import lombok.Data;
import java.util.Date;

@Data
public class UserPerformanceDto {

    private Long id;

    private String musicTitle;
    private Date performDate;
    private String videoUrl;
    private String audioUrl;

    public UserPerformanceDto() {}

    public UserPerformanceDto(UserPerformance userPerformance){
        this.id = userPerformance.getId();
        this.musicTitle = userPerformance.getMusicTitle();
        this.performDate = userPerformance.getPerformDate();
        this.videoUrl = userPerformance.getVideoUrl();
        this.audioUrl = userPerformance.getAudioUrl();
    }

}
