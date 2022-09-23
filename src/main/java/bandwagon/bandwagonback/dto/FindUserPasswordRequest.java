package bandwagon.bandwagonback.dto;

import lombok.Data;

@Data
public class FindUserPasswordRequest {

    private String name;
    private String email;

}
