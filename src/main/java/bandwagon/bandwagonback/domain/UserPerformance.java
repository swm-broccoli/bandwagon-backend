package bandwagon.bandwagonback.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class UserPerformance implements Serializable {
    private String musicTitle;
    private Date performDate;
    private String video_url;
    private String audio_url;
}
