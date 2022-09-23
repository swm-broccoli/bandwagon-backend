package bandwagon.bandwagonback.dto;

import bandwagon.bandwagonback.dto.exception.CustomException;

public class UserHasPostException extends CustomException {
    
    public UserHasPostException() {
        super("이미 구직 게시글을 작성하셨습니다");
    }
}
