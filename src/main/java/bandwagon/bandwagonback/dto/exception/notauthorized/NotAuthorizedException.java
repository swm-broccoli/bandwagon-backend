package bandwagon.bandwagonback.dto.exception.notauthorized;

import bandwagon.bandwagonback.dto.exception.CustomException;
import org.springframework.http.HttpStatus;

public class NotAuthorizedException extends CustomException {

    public NotAuthorizedException(String message) {
        super(message);
    }

    public NotAuthorizedException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
