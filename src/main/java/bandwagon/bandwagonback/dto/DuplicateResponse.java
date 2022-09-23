package bandwagon.bandwagonback.dto;

import lombok.Data;

@Data
public class DuplicateResponse {

    private String email;
    public DuplicateResponse(String email) {
        this.email = email;
    }
}
