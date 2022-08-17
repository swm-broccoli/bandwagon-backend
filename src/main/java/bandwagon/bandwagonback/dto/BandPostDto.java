package bandwagon.bandwagonback.dto;

import bandwagon.bandwagonback.domain.User;
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
    private Boolean isLiked;

    public BandPostDto() {}

    public BandPostDto(BandPost post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.body = post.getBody();
        this.dtype = post.getDtype();
        this.bandId = post.getBand().getId();
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
