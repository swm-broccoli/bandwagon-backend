package bandwagon.bandwagonback.domain;

import bandwagon.bandwagonback.domain.prerequisite.GenrePrerequisite;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "genres")
@Getter
@Setter
public class Genre {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String genre;

    @ManyToMany(mappedBy = "genres")
    private List<User> users = new ArrayList<>();

    @ManyToMany(mappedBy = "genres")
    private List<Band> bands = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "genre_prerequisite_id")
    private GenrePrerequisite genrePrerequisite;
}
