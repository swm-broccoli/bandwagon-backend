package bandwagon.bandwagonback.dto;

import bandwagon.bandwagonback.domain.User;
import bandwagon.bandwagonback.domain.post.UserPost;
import bandwagon.bandwagonback.dto.subdto.IdNameForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserPostDto extends PostDto {
    private String nickname;
    private String userAvatarUrl;
    private List<IdNameForm> tagInfo;

    public UserPostDto() {}

    public UserPostDto(UserPost post) {
        super(post);
        this.nickname = post.getUser().getNickname();
        this.userAvatarUrl = post.getUser().getUserInfo().getAvatarUrl();
        this.tagInfo = post.getUser().getPositions().stream().map(IdNameForm::new).sorted((a, b) -> (int) (a.getId() - b.getId())).limit(3).collect(Collectors.toList());
    }

    public static UserPostDto makeUserPostDto(UserPost post, User user) {
        UserPostDto userPostDto = new UserPostDto(post);
        if (user.getLikedPosts().contains(post)) {
            userPostDto.setIsLiked(true);
        }
        return userPostDto;
    }
}
