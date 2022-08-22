package bandwagon.bandwagonback.repository;

import bandwagon.bandwagonback.domain.Request;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<Request, Long> {
}
