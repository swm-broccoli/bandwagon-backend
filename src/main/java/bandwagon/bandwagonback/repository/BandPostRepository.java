package bandwagon.bandwagonback.repository;

import bandwagon.bandwagonback.domain.Band;
import bandwagon.bandwagonback.domain.post.BandPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface BandPostRepository extends JpaRepository<BandPost, Long>, JpaSpecificationExecutor<BandPost> {
    BandPost findFirstByBand(Band band);
    BandPost findFirstByBand_id(Long id);
    Optional<BandPost> findFirstByIdAndBand_id(Long postId, Long bandId);
}
