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
    //TODO: 나중엔 필요한 테그 정보만 담게 변경
    private MyPageDto tagInfo;

    public UserPostDto() {}

    public UserPostDto(UserPost post) {
        super(post);
        this.nickname = post.getUser().getNickname();
        this.userAvatarUrl = post.getUser().getUserInfo().getAvatarUrl();
        this.tagInfo = new MyPageDto(post.getUser());
    }

    public static UserPostDto makeUserPostDto(UserPost post, User user) {
        UserPostDto userPostDto = new UserPostDto(post);
        if (user.getLikedPosts().contains(post)) {
            userPostDto.setIsLiked(true);
        }
        return userPostDto;
    }
}
