package bandwagon.bandwagonback.dto;

import lombok.Data;

import java.util.List;

@Data
public class PopularPostsDto {

    private List<PostDto> posts;

    public PopularPostsDto(List<PostDto> posts) {
        this.posts = posts;
    }
}
