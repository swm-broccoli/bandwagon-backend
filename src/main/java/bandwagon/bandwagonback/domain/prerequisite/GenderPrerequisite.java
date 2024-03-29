package bandwagon.bandwagonback.domain.prerequisite;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Getter
@Setter
@DiscriminatorValue("Gender")
public class GenderPrerequisite extends BandPrerequisite{

    private Boolean gender;

    public GenderPrerequisite() {}

    public GenderPrerequisite(Boolean gender) {
        this.gender = gender;
    }
}
