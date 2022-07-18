package bandwagon.bandwagonback.dto;

import bandwagon.bandwagonback.domain.User;
import bandwagon.bandwagonback.domain.UserPerformance;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class MyPageRequest {
    private List<String> position;
    private List<String> area;
    private List<String> genre;
    private String description;
    private List<UserPerformance> userPerformances;

    public MyPageRequest() {}

    public MyPageRequest(User user) {
        this.position = user.getUserInfo().getPosition();
        this.area = user.getUserInfo().getArea();
        this.genre = user.getUserInfo().getGenre();
        this.description = user.getUserInfo().getDescription();
        this.userPerformances = user.getUserInfo().getUserPerformances();
    }
}
