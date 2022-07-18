package bandwagon.bandwagonback.dto;

import bandwagon.bandwagonback.domain.User;
import bandwagon.bandwagonback.domain.UserPerformance;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class MyPageDto {
    private String name;
    private Date birthday;
    private List<String> position;
    private List<String> area;
    private List<String> genre;
    private String description;
    private List<UserPerformance> userPerformances;

    public MyPageDto() {}

    public MyPageDto(User user) {
        this.name = user.getName();
        this.birthday = user.getBirthday();
        this.position = user.getUserInfo().getPosition();
        this.area = user.getUserInfo().getArea();
        this.description = user.getUserInfo().getDescription();
        this.userPerformances = user.getUserPerformances();
    }
}
