package bandwagon.bandwagonback.repository;

import bandwagon.bandwagonback.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
