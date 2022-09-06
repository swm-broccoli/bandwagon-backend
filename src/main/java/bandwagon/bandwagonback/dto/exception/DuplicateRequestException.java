package bandwagon.bandwagonback.dto.exception;

public class DuplicateRequestException extends CustomException {
    
    public DuplicateRequestException() {
        super("이미 동일한 요청을 보냈습니다");
    }
}
