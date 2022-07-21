package bandwagon.bandwagonback.dto;

import lombok.Data;

@Data
public class ImageResponseDto {

    private String imgUrl;
    public ImageResponseDto(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
