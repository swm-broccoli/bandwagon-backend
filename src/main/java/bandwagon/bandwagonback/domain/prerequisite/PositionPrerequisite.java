package bandwagon.bandwagonback.domain.prerequisite;

import bandwagon.bandwagonback.domain.Position;
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
@DiscriminatorValue("Position")
public class PositionPrerequisite extends BandPrerequisite {

    @OneToMany(mappedBy = "positionPrerequisite")
    private List<Position> positions = new ArrayList<>();

}
