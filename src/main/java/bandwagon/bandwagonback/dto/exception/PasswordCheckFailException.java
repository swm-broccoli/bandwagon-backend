package bandwagon.bandwagonback.dto.exception;

public class PasswordCheckFailException extends CustomException {

    public PasswordCheckFailException() {
        super("비밀번호를 동일하게 작성해주세요");
    }
}
