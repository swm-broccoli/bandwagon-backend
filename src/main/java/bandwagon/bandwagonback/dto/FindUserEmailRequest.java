package bandwagon.bandwagonback.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class FindUserEmailRequest {

    private String name;
    private LocalDate birthday;
}
