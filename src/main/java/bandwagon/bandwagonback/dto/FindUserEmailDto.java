package bandwagon.bandwagonback.dto;

import lombok.Data;

import java.util.List;

@Data
public class FindUserEmailDto {

    private List<String> emails;

    public FindUserEmailDto(List<String> emails) {
        this.emails = emails;
    }
}
