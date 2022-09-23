package bandwagon.bandwagonback.domain;

import bandwagon.bandwagonback.listener.BandPhotoEntityListener;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.event.EventListener;

import javax.persistence.*;

@Entity
@Table(name = "band_photos")
@Getter @Setter
@EntityListeners(BandPhotoEntityListener.class)
public class BandPhoto {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "band_id")
    private Band band;

    @Column(columnDefinition="TEXT")
    private String imgUrl;

    public BandPhoto() {}

    public BandPhoto(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
