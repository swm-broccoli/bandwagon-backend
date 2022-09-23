package bandwagon.bandwagonback.dto;

import bandwagon.bandwagonback.domain.BandGig;
import bandwagon.bandwagonback.domain.BandPractice;
import bandwagon.bandwagonback.domain.UserPerformance;
import bandwagon.bandwagonback.dto.subdto.SiteUrlForm;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
public class PerformanceDto {

    private Long id;

    private String musicTitle;
    private LocalDate performDate;
    private List<SiteUrlForm> urls;

    public PerformanceDto() {}

    public PerformanceDto(UserPerformance userPerformance){
        this.id = userPerformance.getId();
        this.musicTitle = userPerformance.getMusicTitle();
        this.performDate = userPerformance.getPerformDate();
        this.urls = userPerformance.getUrls();
    }
    public PerformanceDto(BandGig bandGig){
        this.id = bandGig.getId();
        this.musicTitle = bandGig.getMusicTitle();
        this.performDate = bandGig.getPerformDate();
        this.urls = bandGig.getUrls();
    }
    public PerformanceDto(BandPractice bandPractice){
        this.id = bandPractice.getId();
        this.musicTitle = bandPractice.getMusicTitle();
        this.performDate = bandPractice.getPerformDate();
        this.urls = bandPractice.getUrls();
    }

}
