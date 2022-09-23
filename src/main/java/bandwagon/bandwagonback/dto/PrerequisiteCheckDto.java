package bandwagon.bandwagonback.dto;

import bandwagon.bandwagonback.domain.prerequisite.*;
import lombok.Data;

@Data
public class PrerequisiteCheckDto extends PrerequisiteDto{
    private Boolean check;

    public PrerequisiteCheckDto(AgePrerequisite agePrerequisite, Boolean check) {
        super(agePrerequisite);
        this.check = check;
    }

    public PrerequisiteCheckDto(AreaPrerequisite areaPrerequisite, Boolean check) {
        super(areaPrerequisite);
        this.check = check;
    }

    public PrerequisiteCheckDto(GenderPrerequisite genderPrerequisite, Boolean check) {
        super(genderPrerequisite);
        this.check = check;
    }

    public PrerequisiteCheckDto(GenrePrerequisite genrePrerequisite, Boolean check) {
        super(genrePrerequisite);
        this.check = check;
    }

    public PrerequisiteCheckDto(PositionPrerequisite positionPrerequisite, Boolean check) {
        super(positionPrerequisite);
        this.check = check;
    }
}
