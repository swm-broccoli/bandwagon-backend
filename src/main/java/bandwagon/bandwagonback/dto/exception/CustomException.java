package bandwagon.bandwagonback.dto.exception;

import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException {

    private final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    private final Long errorCode;


    public CustomException(Long errorCode) {
        this.errorCode = errorCode;
    }

    public CustomException(Long errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
