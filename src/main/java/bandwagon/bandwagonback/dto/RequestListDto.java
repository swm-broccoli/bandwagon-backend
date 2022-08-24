package bandwagon.bandwagonback.dto;

import lombok.Data;

import java.util.List;

@Data
public class RequestListDto {

    private List<RequestDto> requests;

    public RequestListDto(List<RequestDto> requests) {
        this.requests = requests;
    }
}
