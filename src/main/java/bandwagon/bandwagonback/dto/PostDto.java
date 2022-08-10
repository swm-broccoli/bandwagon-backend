package bandwagon.bandwagonback.dto;

import bandwagon.bandwagonback.domain.post.BandPost;
import bandwagon.bandwagonback.domain.post.Post;
import bandwagon.bandwagonback.domain.post.UserPost;
import lombok.Data;

@Data
public class PostDto {
    private Long id;
    private String title;
    private String body;
    private String dtype;
    private String userEmail;
    private Long bandId;

    public PostDto() {}

    public PostDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.body = post.getBody();
        this.dtype = post.getDtype();
        if (post.getDtype().equals("User")) {
            this.userEmail = ((UserPost) post).getUser().getEmail();
        } else {
            this.bandId = ((BandPost) post).getBand().getId();
        }
    }
}
