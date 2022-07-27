package bandwagon.bandwagonback.domain.post;

import bandwagon.bandwagonback.domain.Band;
import bandwagon.bandwagonback.domain.prerequisite.BandPrerequisite;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@DiscriminatorValue("Band")
public class BandPost extends Post{

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "band_id")
    private Band band;

    @OneToMany(mappedBy = "bandPost", fetch = FetchType.LAZY)
    private List<BandPrerequisite> bandPrerequisites = new ArrayList<>();
}
