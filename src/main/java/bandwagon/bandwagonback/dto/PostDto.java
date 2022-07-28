package bandwagon.bandwagonback.dto;

import lombok.Data;

@Data
public class PostDto {
    private Long id;
    private String title;
    private String body;

    public PostDto() {}
}
