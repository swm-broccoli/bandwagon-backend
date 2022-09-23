package bandwagon.bandwagonback.repository;

import bandwagon.bandwagonback.domain.Position;
import bandwagon.bandwagonback.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PositionRepository extends JpaRepository<Position, Long> {
    public List<Position> findByUsers(User user);
    public List<Position> findByUsers_email(String email);
}
