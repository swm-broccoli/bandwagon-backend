package bandwagon.bandwagonback.dto.exception;

public class DuplicateUserException extends CustomException {
    
    public DuplicateUserException() {
        super("이미 존재하는 회원입니다");
    }
}
