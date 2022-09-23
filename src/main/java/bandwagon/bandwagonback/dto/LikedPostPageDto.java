package bandwagon.bandwagonback.dto;

import lombok.Data;

import java.util.List;

@Data
public class LikedPostPageDto {

    private List<PostDto> posts;
    private int currentPage;
    private long totalItems;
    private int totalPages;

    public LikedPostPageDto(List<PostDto> posts, int currentPage, long totalItems, int totalPages) {
        this.posts = posts;
        this.currentPage = currentPage;
        this.totalItems = totalItems;
        this.totalPages = totalPages;
    }
}
