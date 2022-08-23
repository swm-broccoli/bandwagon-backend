package bandwagon.bandwagonback.repository;

import bandwagon.bandwagonback.domain.Notification;
import bandwagon.bandwagonback.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByReceivingUser(User user);
    long countByReceivingUserAndIsReadFalse(User user);
    @Modifying(flushAutomatically = true)
    @Query("update Notification n set n.isRead = true where n.receivingUser = :user and n.isRead = false")
    void setIsReadOfAllNotificationsByUser(@Param("user") User user);
}
