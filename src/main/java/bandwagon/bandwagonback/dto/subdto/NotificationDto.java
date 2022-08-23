package bandwagon.bandwagonback.dto.subdto;

import bandwagon.bandwagonback.domain.Notification;
import lombok.Data;

@Data
public class NotificationDto {

    private Long notificationId;
    private String message;
    private Boolean isRead;

    public NotificationDto(){}

    public NotificationDto(Notification notification) {
        this.notificationId = notification.getId();
        this.message = notification.createMessage();
        this.isRead = notification.getIsRead();
    }
}
