package bandwagon.bandwagonback.dto;

import bandwagon.bandwagonback.domain.Band;
import bandwagon.bandwagonback.domain.Request;
import bandwagon.bandwagonback.domain.RequestType;
import bandwagon.bandwagonback.domain.User;
import bandwagon.bandwagonback.domain.post.Post;
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
        this.user = new BasicInfoDto(request.getUser());
        this.band = new BasicInfoDto(request.getBand());
        this.post = new IdNameForm(request.getBandPost());
        this.type = request.getType();
    }
}
