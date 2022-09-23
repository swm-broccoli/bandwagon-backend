package bandwagon.bandwagonback.dto.exception.notof;

import bandwagon.bandwagonback.dto.exception.CustomException;
import org.springframework.http.HttpStatus;

public class NotOfException extends CustomException {

    public NotOfException(String message) {
        super(message + " 아닙니다");
    }

    public NotOfException(String message, HttpStatus httpStatus) {
        super(message + " 아닙니다", httpStatus);
    }
}
