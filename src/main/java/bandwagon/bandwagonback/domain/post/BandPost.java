package bandwagon.bandwagonback.domain.post;

import bandwagon.bandwagonback.domain.Band;
import bandwagon.bandwagonback.domain.prerequisite.BandPrerequisite;
import bandwagon.bandwagonback.dto.PostDto;
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

    @OneToMany(mappedBy = "bandPost", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<BandPrerequisite> bandPrerequisites = new ArrayList<>();

    public BandPost() {}

    public BandPost(PostDto postDto) {
        super(postDto.getTitle(), postDto.getBody());
    }

    // Prerequisite 추가
    public void addPrerequisite(BandPrerequisite bandPrerequisite) {
        if (!this.bandPrerequisites.contains(bandPrerequisite)) {
            this.bandPrerequisites.add(bandPrerequisite);
            bandPrerequisite.setBandPost(this);
        }
    }
}
