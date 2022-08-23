package bandwagon.bandwagonback.repository;

import bandwagon.bandwagonback.domain.Notification;
import bandwagon.bandwagonback.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByReceivingUser(User user);
}
