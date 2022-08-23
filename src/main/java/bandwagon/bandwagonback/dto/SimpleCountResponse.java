package bandwagon.bandwagonback.dto;

import lombok.Data;

@Data
public class SimpleCountResponse {

    private long count;

    public SimpleCountResponse(long count) {
        this.count = count;
    }
}
