package bandwagon.bandwagonback.dto.exception.notfound;

public class GenreNotFoundException extends NotFoundException {
    public GenreNotFoundException() {
        super("장르를");
    }
}
