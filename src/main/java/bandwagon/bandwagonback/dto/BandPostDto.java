package bandwagon.bandwagonback.dto;

import bandwagon.bandwagonback.domain.post.BandPost;
import lombok.Data;

@Data
public class BandPostDto {
    private Long id;
    private String title;
    private String body;
    private String dtype;
    private Long bandId;
    private String bandName;
    private String bandAvatarUrl;

    public BandPostDto() {}

    public BandPostDto(BandPost post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.body = post.getBody();
        this.dtype = post.getDtype();
        this.bandId = post.getBand().getId();
        this.bandName = post.getBand().getName();
        this.bandAvatarUrl = post.getBand().getAvatarUrl();
    }
}
