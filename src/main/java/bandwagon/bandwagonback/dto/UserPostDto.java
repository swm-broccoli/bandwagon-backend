package bandwagon.bandwagonback.dto;

import bandwagon.bandwagonback.domain.post.UserPost;
import lombok.Data;

@Data
public class UserPostDto {

    private Long id;
    private String title;
    private String body;
    private String dtype;
    private Long userId;
    private String nickname;
    private String userAvatarUrl;

    public UserPostDto() {}

    public UserPostDto(UserPost post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.body = post.getBody();
        this.dtype = post.getDtype();
        this.userId = post.getUser().getId();
        this.nickname = post.getUser().getNickname();
        this.userAvatarUrl = post.getUser().getUserInfo().getAvatarUrl();
    }
}
