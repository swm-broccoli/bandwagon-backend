package bandwagon.bandwagonback.dto.exception.notfound;

import bandwagon.bandwagonback.dto.exception.CustomException;
import org.springframework.http.HttpStatus;

public class NotFoundException extends CustomException {

    public NotFoundException(String message) {
        super(message + " 찾을 수 없습니다");
    }

    public NotFoundException(String message, HttpStatus httpStatus) {
        super(message + " 찾을 수 없습니다", httpStatus);
    }

}
