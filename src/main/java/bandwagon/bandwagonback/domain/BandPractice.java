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
@Table(name = "band_practices")
@Getter @Setter
@TypeDefs({
        @TypeDef(name = "json", typeClass = JsonType.class)
})
public class BandPractice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String musicTitle;
    private Date performDate;

    @Type(type = "json")
    @Column(columnDefinition = "jsonb")
    private List<SiteUrlForm> urls = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "band_id")
    private Band band;

    public BandPractice() {}

    public BandPractice(PerformanceDto performanceDto) {
        this.musicTitle = performanceDto.getMusicTitle();
        this.performDate = performanceDto.getPerformDate();
        this.urls = performanceDto.getUrls();
    }

    public void update(PerformanceDto performanceDto) {
        this.musicTitle = performanceDto.getMusicTitle();
        this.performDate = performanceDto.getPerformDate();
        this.urls = performanceDto.getUrls();
    }
}
