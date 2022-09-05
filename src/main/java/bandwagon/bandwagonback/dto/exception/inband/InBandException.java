package bandwagon.bandwagonback.dto.exception.inband;

import bandwagon.bandwagonback.dto.exception.CustomException;
import org.springframework.http.HttpStatus;

public class InBandException extends CustomException {

    public InBandException(String message) {
        super(message);
    }

    public InBandException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
