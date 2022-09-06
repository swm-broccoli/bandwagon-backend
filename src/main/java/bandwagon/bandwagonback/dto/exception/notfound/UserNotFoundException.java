package bandwagon.bandwagonback.dto.exception.notfound;


public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException() {
        super("유저를");
    }
}
