package bandwagon.bandwagonback.api;

import bandwagon.bandwagonback.domain.User;
import bandwagon.bandwagonback.dto.NotificationListDto;
import bandwagon.bandwagonback.dto.SimpleCountResponse;
import bandwagon.bandwagonback.service.NotificationService;
import bandwagon.bandwagonback.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "PostApiController")
@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class NotificationApiController {

    private final NotificationService notificationService;
    private final UserService userService;

    @Operation(description = "안읽은 알림 수 조회")
    @GetMapping("/api/notifications/count")
    public ResponseEntity<?> getUnreadNotiCount(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        User user = userService.findOneByEmail(email);
        long count = notificationService.getUnreadNotificationCount(user);
        return ResponseEntity.ok(new SimpleCountResponse(count));
    }

    @Operation(description = "알림 조회")
    @GetMapping("/api/notifications")
    public ResponseEntity<?> getUserNotifications(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        User user = userService.findOneByEmail(email);
        NotificationListDto notificationListDto = notificationService.getNotificationToUser(user);
        return ResponseEntity.ok(notificationListDto);
    }

}
