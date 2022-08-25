package bandwagon.bandwagonback.dto.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum NotFoundErrorCode implements ErrorCode{

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다"),
    BAND_NOT_FOUND(HttpStatus.NOT_FOUND, "밴드를 찾을 수 없습니다"),
    AREA_NOT_FOUND(HttpStatus.NOT_FOUND, "지역을 찾을 수 없습니다"),
    POSITION_NOT_FOUND(HttpStatus.NOT_FOUND, "역할을 찾을 수 없습니다"),
    GENRE_NOT_FOUND(HttpStatus.NOT_FOUND, "장르를 찾을 수 없습니다"),
    DAY_NOT_FOUND(HttpStatus.NOT_FOUND, "요일을 찾을 수 없습니다"),
    BAND_PRACTICE_NOT_FOUND(HttpStatus.NOT_FOUND, "밴드 합주기록을 찾을 수 없습니다"),
    USER_PERFORMANCE_NOT_FOUND(HttpStatus.NOT_FOUND, "유저 연주기록을 찾을 수 없습니다"),
    BAND_GIG_NOT_FOUND(HttpStatus.NOT_FOUND, "밴드 공연기록을 찾을 수 없습니다"),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다"),
    FRONTMAN_NOT_FOUND(HttpStatus.NOT_FOUND, "프런트맨을 찾을 수 없습니다")
    ;


    private final HttpStatus httpStatus;
    private final String errorMessage;
}
