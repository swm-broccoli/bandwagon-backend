package bandwagon.bandwagonback.dto.subdto;

import bandwagon.bandwagonback.domain.BandPhoto;
import bandwagon.bandwagonback.domain.Day;
import bandwagon.bandwagonback.domain.Genre;
import bandwagon.bandwagonback.domain.Position;
import lombok.Data;

@Data
public class IdNameForm {
    private Long id;
    private String name;

    public IdNameForm() {}

    public IdNameForm(Position position) {
        this.id = position.getId();
        this.name = position.getPosition();
    }

    public IdNameForm(Genre genre) {
        this.id = genre.getId();
        this.name = genre.getGenre();
    }

    public IdNameForm(Day day) {
        this.id = day.getId();
        this.name = day.getDay();
    }

    public IdNameForm(BandPhoto bandPhoto) {
        this.id = bandPhoto.getId();
        this.name = bandPhoto.getImgUrl();
    }
}
