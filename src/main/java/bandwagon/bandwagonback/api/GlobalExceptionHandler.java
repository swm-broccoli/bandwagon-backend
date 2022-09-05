package bandwagon.bandwagonback.api;

import bandwagon.bandwagonback.dto.ErrorResponse;
import bandwagon.bandwagonback.dto.exception.InvalidTypeException;
import bandwagon.bandwagonback.dto.exception.inband.InBandException;
import bandwagon.bandwagonback.dto.exception.inband.UserInBandException;
import bandwagon.bandwagonback.dto.exception.inband.UserNotInBandException;
import bandwagon.bandwagonback.dto.exception.notfound.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(NotFoundException e) {
        log.error("handleNotFoundException", e);
        return ResponseEntity.status(e.getHttpStatus()).body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentialsException(BadCredentialsException e) {
        log.error("handleBadCredentialsException", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("비밀번호가 올바르지 않습니다!"));
    }

    @ExceptionHandler(InBandException.class)
    public ResponseEntity<?> handleInBandException(InBandException e) {
        log.error("handleInBandException", e);
        return ResponseEntity.status(e.getHttpStatus()).body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(InvalidTypeException.class)
    public ResponseEntity<?> handleInvalidTypeException(InvalidTypeException e) {
        log.error("handleInvalidTypeException", e);
        return ResponseEntity.status(e.getHttpStatus()).body(new ErrorResponse(e.getMessage()));
    }
}
