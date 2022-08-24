package bandwagon.bandwagonback.repository;

import bandwagon.bandwagonback.domain.Request;
import bandwagon.bandwagonback.domain.RequestType;
import bandwagon.bandwagonback.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findAllByUserAndType(User user, RequestType type);
}
