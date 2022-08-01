package bandwagon.bandwagonback.dto.subdto;

import bandwagon.bandwagonback.domain.BandPhoto;
import bandwagon.bandwagonback.domain.Genre;
import bandwagon.bandwagonback.domain.Position;
import lombok.Data;

@Data
public class IdNameForm {
    private Long id;
    private String name;

    public IdNameForm(Position position) {
        this.id = position.getId();
        this.name = position.getPosition();
    }

    public IdNameForm(Genre genre) {
        this.id = genre.getId();
        this.name = genre.getGenre();
    }

    public IdNameForm(BandPhoto bandPhoto) {
        this.id = bandPhoto.getId();
        this.name = bandPhoto.getImgUrl();
    }
}
