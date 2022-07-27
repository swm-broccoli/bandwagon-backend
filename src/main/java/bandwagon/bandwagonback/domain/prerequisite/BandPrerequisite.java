package bandwagon.bandwagonback.domain.prerequisite;

import bandwagon.bandwagonback.domain.post.BandPost;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "band_prerequisites")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter @Setter
public abstract class BandPrerequisite {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "band_prerequisite_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private BandPost bandPost;
}
