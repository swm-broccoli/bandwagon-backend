package bandwagon.bandwagonback.domain.prerequisite;

import bandwagon.bandwagonback.domain.Genre;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@DiscriminatorValue("Genre")
public class GenrePrerequisite extends BandPrerequisite {

    @ManyToMany
    @JoinTable(name = "prerequisite_genres",
            joinColumns = @JoinColumn(name = "prerequisite_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private List<Genre> genres = new ArrayList<>();
}
