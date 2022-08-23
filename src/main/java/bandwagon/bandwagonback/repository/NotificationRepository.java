package bandwagon.bandwagonback.repository;

import bandwagon.bandwagonback.domain.Notification;
import bandwagon.bandwagonback.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByReceivingUser(User user);
    long countByReceivingUserAndIsReadFalse(User user);
    @Modifying(clearAutomatically = true)
    @Query(value = "update Notification n set n.isRead = true where n.user = :user and n.isRead = false", nativeQuery = true)
    void setIsReadOfAllNotificationsByUser(User user);
}
