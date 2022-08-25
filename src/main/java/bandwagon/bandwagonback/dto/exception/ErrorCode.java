package bandwagon.bandwagonback.dto.exception;

import org.springframework.http.HttpStatus;

public interface ErrorCode {

    String name();
    HttpStatus getHttpStatus();
    String getErrorMessage();
}
