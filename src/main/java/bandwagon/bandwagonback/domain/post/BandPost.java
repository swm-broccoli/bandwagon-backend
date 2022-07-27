package bandwagon.bandwagonback.domain.post;

import bandwagon.bandwagonback.domain.Band;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
@Getter @Setter
@DiscriminatorValue("Band")
public class BandPost extends Post{

    @OneToOne
    @JoinColumn(name = "band_id")
    private Band band;
}
