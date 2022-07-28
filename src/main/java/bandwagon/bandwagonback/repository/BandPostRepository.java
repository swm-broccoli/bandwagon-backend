package bandwagon.bandwagonback.repository;

import bandwagon.bandwagonback.domain.post.BandPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BandPostRepository extends JpaRepository<BandPost, Long> {
}
