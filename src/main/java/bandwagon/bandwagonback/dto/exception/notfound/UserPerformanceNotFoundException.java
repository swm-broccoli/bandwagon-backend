package bandwagon.bandwagonback.dto.exception.notfound;

public class UserPerformanceNotFoundException extends NotFoundException {
    public UserPerformanceNotFoundException() {
        super("유저 연주기록을");
    }
}
