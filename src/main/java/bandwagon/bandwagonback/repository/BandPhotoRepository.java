package bandwagon.bandwagonback.repository;

import bandwagon.bandwagonback.domain.BandPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BandPhotoRepository extends JpaRepository<BandPhoto, Long> {
}
