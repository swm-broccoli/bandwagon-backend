package bandwagon.bandwagonback.dto;

import lombok.Data;

import java.util.List;

@Data
public class BandPostPageDto {

    private List<PostDto> posts;
    private int currentPage;
    private long totalItems;
    private int totalPages;

    public BandPostPageDto(List<PostDto> posts, int currentPage, long totalItems, int totalPages) {
        this.posts = posts;
        this.currentPage = currentPage;
        this.totalItems = totalItems;
        this.totalPages = totalPages;
    }
}
