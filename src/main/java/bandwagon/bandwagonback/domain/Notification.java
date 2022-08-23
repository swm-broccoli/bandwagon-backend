package bandwagon.bandwagonback.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter @Setter
@EntityListeners(AuditingEntityListener.class)
public class Notification {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private User sendingUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private User receivingUser;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    public Notification(){}

    public Notification(User sendingUser, User receivingUser, NotificationType notificationType) {
        this.sendingUser = sendingUser;
        this.receivingUser = receivingUser;
        this.type = notificationType;
    }

    public String createMessage(){
        switch (type) {
            case APPLY:
                return sendingUser.getNickname() + "님이 밴드에 가입 요청을 보냈습니다!";
            case APPLY_ACCEPT:
                return "밴드 가입 요청이 승인되었습니다!";
            case APPLY_DECLINE:
                return "밴드 가입 요청이 거절되었습니다!";
            case INVITE:
                return sendingUser.getNickname() + "님이 밴드 초대 요청을 보냈습니다!";
            case INVITE_ACCEPT:
                return sendingUser.getNickname() + "님이 밴드 초대 요청을 수락했습니다!";
            case INVITE_DECLINE:
                return sendingUser.getNickname() + "님이 밴드 초대 요청을 거절했습니다!";
            case DISBAND:
                return sendingUser.getNickname() + "님이 밴드를 해체했습니다!";
            case KICK:
                return sendingUser.getNickname() + "님이 " + receivingUser.getNickname() + "님을 밴드에서 강퇴했습니다!";
            case WITHDRAW:
                return sendingUser.getNickname() + "님이 밴드에서 나가셨습니다!";
            default:
                return null;
        }
    }
}
