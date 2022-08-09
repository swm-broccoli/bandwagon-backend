package bandwagon.bandwagonback.domain;

import bandwagon.bandwagonback.domain.prerequisite.GenrePrerequisite;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "genres")
@Getter
@Setter
public class Genre {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String genre;

    @ManyToMany(mappedBy = "genres")
    private Set<User> users = new HashSet<>();

    @ManyToMany(mappedBy = "genres")
    private List<Band> bands = new ArrayList<>();

    @ManyToMany(mappedBy = "genres")
    private List<GenrePrerequisite> genrePrerequisites = new ArrayList<>();
}
