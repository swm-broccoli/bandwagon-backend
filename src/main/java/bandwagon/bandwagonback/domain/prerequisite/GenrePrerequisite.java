package bandwagon.bandwagonback.domain.prerequisite;

import bandwagon.bandwagonback.domain.Area;
import bandwagon.bandwagonback.domain.Genre;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@DiscriminatorValue("Genre")
public class GenrePrerequisite extends BandPrerequisite {

    @ManyToMany
    @JoinTable(name = "prerequisite_genres",
            joinColumns = @JoinColumn(name = "prerequisite_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private Set<Genre> genres = new HashSet<>();

    // 지원 자격 장르 추가
    public void addGenre(Genre genre) {
        if (!this.genres.contains(genre)) {
            genres.add(genre);
            genre.getGenrePrerequisites().add(this);
        }
    }
    // 지원 자격 장르 제거
    public void removeGenre(Genre genre) {
        this.genres.remove(genre);
        genre.getGenrePrerequisites().remove(this);
    }
}
