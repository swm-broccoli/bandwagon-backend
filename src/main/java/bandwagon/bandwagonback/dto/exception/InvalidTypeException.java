package bandwagon.bandwagonback.dto.exception;

public class InvalidTypeException extends CustomException {
    
    public InvalidTypeException() {
        super("존재하지 않는 type 입니다");
    }
}
