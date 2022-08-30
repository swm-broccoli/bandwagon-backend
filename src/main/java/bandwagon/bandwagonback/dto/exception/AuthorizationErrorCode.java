package bandwagon.bandwagonback.dto.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthorizationErrorCode implements ErrorCode {

    USER_NOT_AUTHORIZED(HttpStatus.UNAUTHORIZED, "유저에게 해당 동작의 권한이 없습니다"),
    SOCIAL_LOGIN_NOT_AUTHORIZED(HttpStatus.UNAUTHORIZED, "소셜 로그인 계정은 해당 동작을 할 수 없습니다"),

    ;

    private final HttpStatus httpStatus;
    private final String errorMessage;
}
