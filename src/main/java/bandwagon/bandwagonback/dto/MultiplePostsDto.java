package bandwagon.bandwagonback.dto;

import lombok.Data;

import java.util.List;

@Data
public class MultiplePostsDto {

    private List<PostDto> posts;

    public MultiplePostsDto(List<PostDto> posts) {
        this.posts = posts;
    }
}
