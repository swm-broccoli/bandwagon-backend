package bandwagon.bandwagonback.dto.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum NotOfErrorCode implements ErrorCode {

    USER_NOT_OF_BAND(HttpStatus.UNAUTHORIZED, "해당 밴드에 속한 유저가 아닙니다"),
    BAND_GIG_NOT_OF_BAND(HttpStatus.UNAUTHORIZED, "해당 밴드의 공연기록이 아닙니다"),
    BAND_PHOTO_NOT_OF_BAND(HttpStatus.UNAUTHORIZED, "해당 밴드의 사진이 아닙니다"),
    BAND_PRACTICE_NOT_OF_BAND(HttpStatus.UNAUTHORIZED, "해당 밴드의 합주기록이 아닙니다"),
    PREREQUISITE_NOT_OF_BAND(HttpStatus.UNAUTHORIZED, "해당 밴드의 지원조건이 아닙니다"),
    USER_PERFORMANCE_NOT_OF_USER(HttpStatus.UNAUTHORIZED, "유저의 연주기록이 아닙니다")
    ;

    private final HttpStatus httpStatus;
    private final String errorMessage;
}
