package bandwagon.bandwagonback.dto.exception.notauthorized;

public class UserNotFrontmanException extends NotAuthorizedException {
    
    public UserNotFrontmanException() {
        super("프런트맨이 아니라 해당 작업을 수행할 수 없습니다");
    }
}
