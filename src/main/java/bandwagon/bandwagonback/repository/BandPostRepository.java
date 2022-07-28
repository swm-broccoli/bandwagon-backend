package bandwagon.bandwagonback.repository;

import bandwagon.bandwagonback.domain.Band;
import bandwagon.bandwagonback.domain.post.BandPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BandPostRepository extends JpaRepository<BandPost, Long> {
    public BandPost findFirstByBand(Band band);
    public BandPost findFirstByBand_id(Long id);
}
