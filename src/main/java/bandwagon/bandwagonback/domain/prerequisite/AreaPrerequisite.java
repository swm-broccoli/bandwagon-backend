package bandwagon.bandwagonback.domain.prerequisite;

import bandwagon.bandwagonback.domain.Area;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@DiscriminatorValue("Area")
public class AreaPrerequisite extends BandPrerequisite{

    @ManyToMany
    @JoinTable(name = "prerequisite_areas",
            joinColumns = @JoinColumn(name = "prerequisite_id"),
            inverseJoinColumns = @JoinColumn(name = "area_id"))
    private List<Area> areas = new ArrayList<>();
}
