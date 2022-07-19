package bandwagon.bandwagonback.repository;

import bandwagon.bandwagonback.domain.User;
import bandwagon.bandwagonback.domain.UserPerformance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserPerformanceRepository extends JpaRepository<UserPerformance, Long> {
    public List<UserPerformance> findByUser(User user);
}
