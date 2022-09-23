package bandwagon.bandwagonback.repository;

import bandwagon.bandwagonback.domain.Band;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BandRepository extends JpaRepository<Band, Long> {
    public Band findByName(String name);
}
