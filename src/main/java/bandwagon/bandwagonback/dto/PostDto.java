package bandwagon.bandwagonback.dto;

import bandwagon.bandwagonback.domain.post.BandPost;
import bandwagon.bandwagonback.domain.post.Post;
import lombok.Data;

@Data
public class PostDto {
    private Long id;
    private String title;
    private String body;

    public PostDto() {}

    public PostDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.body = post.getBody();
    }
}
