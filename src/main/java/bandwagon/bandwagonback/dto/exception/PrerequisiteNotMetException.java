package bandwagon.bandwagonback.dto.exception;

public class PrerequisiteNotMetException extends CustomException {
    
    public PrerequisiteNotMetException() {
        super("지원요건에 맞지 않아 신청하실 수 없습니다");
    }
}
