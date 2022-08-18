package bandwagon.bandwagonback.dto;

import bandwagon.bandwagonback.domain.User;
import bandwagon.bandwagonback.domain.post.BandPost;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BandPostDto extends PostDto{
    private String bandName;
    private String bandAvatarUrl;
    private Boolean isLiked;

    public BandPostDto() {}

    public BandPostDto(BandPost post) {
        super(post);
        this.bandName = post.getBand().getName();
        this.bandAvatarUrl = post.getBand().getAvatarUrl();
        this.isLiked = false;
    }

    public static BandPostDto makeBandPostDto(BandPost post, User user) {
        BandPostDto bandPostDto = new BandPostDto(post);
        if (user.getLikedPosts().contains(post)) {
            bandPostDto.setIsLiked(true);
        }
        return bandPostDto;
    }
}
