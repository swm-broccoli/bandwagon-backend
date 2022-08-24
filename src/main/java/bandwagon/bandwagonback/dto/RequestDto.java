package bandwagon.bandwagonback.dto;

import bandwagon.bandwagonback.domain.Request;
import bandwagon.bandwagonback.domain.RequestType;
import bandwagon.bandwagonback.dto.subdto.BasicInfoDto;
import bandwagon.bandwagonback.dto.subdto.IdNameForm;
import lombok.Data;

@Data
public class RequestDto {

    private Long id;
    private BasicInfoDto user;
    private BasicInfoDto band;
    private IdNameForm post;
    private RequestType type;

    public RequestDto(Request request) {
        this.id = request.getId();
        if (request.getUser() != null) {
            this.user = new BasicInfoDto(request.getUser());
        }
        if (request.getBand() != null) {
            this.band = new BasicInfoDto(request.getBand());
        }
        if (request.getBandPost() != null) {
            this.post = new IdNameForm(request.getBandPost());
        }
        this.type = request.getType();
    }
}
