package bandwagon.bandwagonback.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "band_photos")
@Getter @Setter
public class BandPhoto {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "band_id")
    private Band band;

    @Column(columnDefinition="TEXT")
    private String imgUrl;
}
