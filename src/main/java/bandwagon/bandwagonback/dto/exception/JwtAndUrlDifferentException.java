package bandwagon.bandwagonback.dto.exception;

public class JwtAndUrlDifferentException extends CustomException {
    
    public JwtAndUrlDifferentException() {
        super("로그인 된 유저와 불러오려는 유저가 일치하지 않습니다");
    }
}
