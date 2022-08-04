package bandwagon.bandwagonback.domain;

import bandwagon.bandwagonback.dto.PerformanceDto;
import bandwagon.bandwagonback.dto.subdto.SiteUrlForm;
import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "user_performances")
@Getter @Setter
@TypeDefs({
        @TypeDef(name = "json", typeClass = JsonType.class)
})
public class UserPerformance {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String musicTitle;
    private Date performDate;

    @Type(type = "json")
    @Column(columnDefinition = "jsonb")
    private List<SiteUrlForm> urls = new ArrayList<>();

//    @Column(columnDefinition="TEXT")
//    private String videoUrl;
//
//    @Column(columnDefinition="TEXT")
//    private String audioUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public UserPerformance() {}

    public UserPerformance(PerformanceDto performanceDto) {
        this.musicTitle = performanceDto.getMusicTitle();
        this.performDate = performanceDto.getPerformDate();
//        this.videoUrl = performanceDto.getVideoUrl();
//        this.audioUrl = performanceDto.getAudioUrl();
        this.urls = performanceDto.getUrls();
    }

    public void update(PerformanceDto performanceDto) {
        this.musicTitle = performanceDto.getMusicTitle();
        this.performDate = performanceDto.getPerformDate();
//        this.videoUrl = performanceDto.getVideoUrl();
//        this.audioUrl = performanceDto.getAudioUrl();
        this.urls = performanceDto.getUrls();
    }
}
