package bandwagon.bandwagonback.dto;

import bandwagon.bandwagonback.domain.Band;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BandPostDto {
    private Long id;
    private String title;
    private String body;
    private List<PrerequisiteDto> prerequisites = new ArrayList<>();

    public BandPostDto() {}

    public BandPostDto(Band band) {

    }
}
