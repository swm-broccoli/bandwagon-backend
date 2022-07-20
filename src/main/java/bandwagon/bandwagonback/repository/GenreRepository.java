package bandwagon.bandwagonback.repository;

import bandwagon.bandwagonback.domain.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Long> {

}
