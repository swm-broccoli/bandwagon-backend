package bandwagon.bandwagonback.domain.prerequisite;

import bandwagon.bandwagonback.domain.Area;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@DiscriminatorValue("Area")
public class AreaPrerequisite extends BandPrerequisite{

    @OneToMany(mappedBy = "areaPrerequisite")
    private List<Area> areas = new ArrayList<>();
}
