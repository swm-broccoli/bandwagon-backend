package bandwagon.bandwagonback.dto;

import bandwagon.bandwagonback.domain.Area;
import bandwagon.bandwagonback.domain.Genre;
import bandwagon.bandwagonback.domain.Position;
import bandwagon.bandwagonback.domain.prerequisite.*;
import bandwagon.bandwagonback.dto.subdto.AreaForm;
import bandwagon.bandwagonback.dto.subdto.IdNameForm;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PrerequisiteDto {
    private Long id;
    private String dtype;
    private Integer min;
    private Integer max;
    private Boolean gender;
    private List<AreaForm> areas = new ArrayList<>();
    private List<IdNameForm> genres = new ArrayList<>();
    private List<IdNameForm> positions = new ArrayList<>();

    public PrerequisiteDto(){}

    public PrerequisiteDto(AgePrerequisite agePrerequisite) {
        this.id = agePrerequisite.getId();
        this.dtype = agePrerequisite.getDtype();
        this.min = agePrerequisite.getMin();
        this.max = agePrerequisite.getMax();
    }

    public PrerequisiteDto(AreaPrerequisite areaPrerequisite) {
        this.id = areaPrerequisite.getId();
        this.dtype = areaPrerequisite.getDtype();
        for (Area area : areaPrerequisite.getAreas()) {
            this.areas.add(new AreaForm(area));
        }
    }

    public PrerequisiteDto(GenderPrerequisite genderPrerequisite) {
        this.id = genderPrerequisite.getId();
        this.dtype = genderPrerequisite.getDtype();
        this.gender = genderPrerequisite.getGender();
    }

    public PrerequisiteDto(GenrePrerequisite genrePrerequisite) {
        this.id = genrePrerequisite.getId();
        this.dtype = genrePrerequisite.getDtype();
        for (Genre genre : genrePrerequisite.getGenres()) {
            this.genres.add(new IdNameForm(genre));
        }
    }

    public PrerequisiteDto(PositionPrerequisite positionPrerequisite) {
        this.id = positionPrerequisite.getId();
        this.dtype = positionPrerequisite.getDtype();
        for (Position position : positionPrerequisite.getPositions()) {
            this.positions.add(new IdNameForm(position));
        }
    }

}
