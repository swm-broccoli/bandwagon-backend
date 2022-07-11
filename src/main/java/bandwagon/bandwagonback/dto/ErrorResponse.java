package bandwagon.bandwagonback.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ErrorResponse {

    private String errorMessage;

    public ErrorResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
