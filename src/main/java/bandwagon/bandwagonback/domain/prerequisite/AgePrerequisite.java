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

    private Integer min;
    private Integer max;

    public AgePrerequisite() {}

    public AgePrerequisite(Integer min, Integer max) {
        this.min = min;
        this.max = max;
    }
}
