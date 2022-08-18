package bandwagon.bandwagonback.dto;

import bandwagon.bandwagonback.domain.User;
import bandwagon.bandwagonback.domain.post.UserPost;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserPostDto extends PostDto {
    private String nickname;
    private String userAvatarUrl;
    private Boolean isLiked;

    public UserPostDto() {}

    public UserPostDto(UserPost post) {
        super(post);
        this.nickname = post.getUser().getNickname();
        this.userAvatarUrl = post.getUser().getUserInfo().getAvatarUrl();
        this.isLiked = false;
    }

    public static UserPostDto makeUserPostDto(UserPost post, User user) {
        UserPostDto userPostDto = new UserPostDto(post);
        if (user.getLikedPosts().contains(post)) {
            userPostDto.setIsLiked(true);
        }
        return userPostDto;
    }
}
