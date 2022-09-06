package bandwagon.bandwagonback.dto.exception.notauthorized;

public class UserNotAuthorizedException extends NotAuthorizedException {
    
    public UserNotAuthorizedException() {
        super("유저에게 해당 동작을 수행할 권한이 없습니다");
    }
}
