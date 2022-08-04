package bandwagon.bandwagonback.repository;

import bandwagon.bandwagonback.domain.Day;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DayRepository extends JpaRepository<Day, Long> {
}
