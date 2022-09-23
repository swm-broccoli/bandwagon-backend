package bandwagon.bandwagonback.dto;

import lombok.Data;

@Data
public class SimpleIdResponse {

    private Long id;

    public SimpleIdResponse(Long id) {
        this.id = id;
    }
}
