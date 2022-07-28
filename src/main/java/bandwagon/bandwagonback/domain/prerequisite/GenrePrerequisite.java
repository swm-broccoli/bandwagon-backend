package bandwagon.bandwagonback.domain.prerequisite;

import bandwagon.bandwagonback.domain.Genre;
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
@DiscriminatorValue("Genre")
public class GenrePrerequisite extends BandPrerequisite {

    @OneToMany(mappedBy = "genrePrerequisite")
    private List<Genre> genres = new ArrayList<>();
}
