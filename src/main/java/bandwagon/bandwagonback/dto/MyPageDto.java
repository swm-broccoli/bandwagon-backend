package bandwagon.bandwagonback.dto;

import bandwagon.bandwagonback.domain.*;
import bandwagon.bandwagonback.dto.subdto.AreaForm;
import bandwagon.bandwagonback.dto.subdto.IdNameForm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Data
public class MyPageDto {

    private Long id;
    private String avatarUrl;
    private String name;
    private String nickname;
    private LocalDate birthday;
    private Boolean gender;
    private List<IdNameForm> positions = new ArrayList<>();
    private List<AreaForm> areas = new ArrayList<>();
    private List<IdNameForm> genres = new ArrayList<>();
    private String description;
    private List<PerformanceDto> userPerformances = new ArrayList<>();

    public MyPageDto(User user) {
        this.id = user.getId();
        this.avatarUrl = user.getUserInfo().getAvatarUrl();
        this.name = user.getName();
        this.nickname = user.getNickname();
        this.birthday = user.getBirthday();
        this.gender = user.getGender();
        for(Position position : user.getPositions()) {
            this.positions.add(new IdNameForm(position));
        }
        for(Area area : user.getAreas()) {
            this.areas.add(new AreaForm(area));
        }
        for(Genre genre : user.getGenres()) {
            this.genres.add(new IdNameForm(genre));
        }
        this.description = user.getUserInfo().getDescription();
        for(UserPerformance userPerformance : user.getUserPerformances()) {
            this.userPerformances.add(new PerformanceDto(userPerformance));
        }
    }

}
