package bandwagon.bandwagonback.dto;

import bandwagon.bandwagonback.dto.subdto.NotificationDto;
import lombok.Data;

import java.util.List;

@Data
public class NotificationListDto {

    private List<NotificationDto> notifications;

    public NotificationListDto(List<NotificationDto> notifications) {
        this.notifications = notifications;
    }
}
