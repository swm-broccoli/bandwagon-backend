package bandwagon.bandwagonback.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PrerequisiteDto {
    private Long id;
    private String dtype;
    private Integer min;
    private Integer max;
    private Boolean gender;
    private List<Long> ids = new ArrayList<>();
}
