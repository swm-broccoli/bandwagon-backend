package bandwagon.bandwagonback.dto.exception;

public class UserInBandException extends CustomException {

    public UserInBandException() {
        super("이미 밴드에 속한 유저입니다");
    }
}
