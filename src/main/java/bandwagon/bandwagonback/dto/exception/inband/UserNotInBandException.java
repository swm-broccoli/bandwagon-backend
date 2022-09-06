package bandwagon.bandwagonback.dto.exception.inband;

import org.springframework.http.HttpStatus;

public class UserNotInBandException extends InBandException {

    public UserNotInBandException() {
        super("가입된 밴드가 없습니다", HttpStatus.NOT_FOUND);
    }
}
