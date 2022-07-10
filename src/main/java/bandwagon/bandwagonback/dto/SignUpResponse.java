package bandwagon.bandwagonback.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SignUpResponse {
    @Schema(description = "유저 DB id", example = "1")
    private final Long id;

    public SignUpResponse(Long id) {
        this.id = id;
    }
}
