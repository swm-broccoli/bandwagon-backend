package bandwagon.bandwagonback.dto;

import bandwagon.bandwagonback.domain.post.BandPost;
import lombok.Data;

@Data
public class PostDto {
    private Long id;
    private String title;
    private String body;

    public PostDto() {}

    public PostDto(BandPost bandPost) {
        this.id = bandPost.getId();
        this.title = bandPost.getTitle();
        this.body = bandPost.getBody();
    }
}
