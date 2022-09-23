package bandwagon.bandwagonback.dto.exception.notfound;

public class RequestNotFoundException extends NotFoundException {
    
    public RequestNotFoundException() {
        super("지원/초대 요청 정보를");
    }
}
