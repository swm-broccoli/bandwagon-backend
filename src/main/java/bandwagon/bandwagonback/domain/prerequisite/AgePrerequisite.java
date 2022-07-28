package bandwagon.bandwagonback.domain.prerequisite;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Getter
@Setter
@DiscriminatorValue("Age")
public class AgePrerequisite extends BandPrerequisite {

    private int min;
    private int max;
}
