package bandwagon.bandwagonback.dto.subdto;

import bandwagon.bandwagonback.domain.Notification;
import bandwagon.bandwagonback.domain.NotificationType;
import lombok.Data;

@Data
public class NotificationDto {

    private Long notificationId;
    private String message;
    private Boolean isRead;
    private NotificationType type;

    public NotificationDto(){}

    public NotificationDto(Notification notification) {
        this.notificationId = notification.getId();
        this.message = notification.createMessage();
        this.isRead = notification.getIsRead();
        this.type = notification.getType();
    }
}
