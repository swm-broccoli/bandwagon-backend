package bandwagon.bandwagonback.dto;

import lombok.Data;
import java.util.Date;

@Data
public class UserPerformanceDto {

    private String musicTitle;
    private Date performDate;
    private String videoUrl;
    private String audioUrl;

}
