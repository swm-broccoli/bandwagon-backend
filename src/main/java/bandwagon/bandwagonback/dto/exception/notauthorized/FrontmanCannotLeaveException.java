package bandwagon.bandwagonback.dto.exception.notauthorized;

public class FrontmanCannotLeaveException extends NotAuthorizedException {

    public FrontmanCannotLeaveException() {
        super("프런트맨은 탈퇴/추방 할 수 없습니다");
    }
}
