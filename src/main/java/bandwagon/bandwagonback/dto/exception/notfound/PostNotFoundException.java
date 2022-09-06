package bandwagon.bandwagonback.dto.exception.notfound;

public class PostNotFoundException extends NotFoundException {
    public PostNotFoundException() {
        super("게시글을");
    }
}
