package bandwagon.bandwagonback.dto.exception;

public class InvalidPasswordException extends CustomException {

    public InvalidPasswordException() {
        super("올바른 비밀번호를 입력해주세요");
    }
}
