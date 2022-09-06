package bandwagon.bandwagonback.dto.exception.inband;

public class UserInBandException extends InBandException {

    public UserInBandException() {
        super("이미 밴드에 속한 유저입니다");
    }
}
