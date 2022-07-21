package bandwagon.bandwagonback.dto;

import bandwagon.bandwagonback.domain.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Data
public class MyPageDto {
    private String name;
    private Date birthday;
    private List<IdNameForm> positions = new ArrayList<>();
    private List<AreaForm> areas = new ArrayList<>();
    private List<IdNameForm> genres = new ArrayList<>();
    private String description;
    private List<UserPerformanceDto> userPerformances = new ArrayList<>();

    public MyPageDto(User user) {
        this.name = user.getName();
        this.birthday = user.getBirthday();
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
            this.userPerformances.add(new UserPerformanceDto(userPerformance));
        }
    }


    @Data
    static class IdNameForm {
        private Long id;
        private String name;

        public IdNameForm(Position position) {
            this.id = position.getId();
            this.name = position.getPosition();
        }

        public IdNameForm(Genre genre) {
            this.id = genre.getId();
            this.name = genre.getGenre();
        }
    }

    @Data
    static class AreaForm {
        private Long id;
        private String city;
        private String district;

        public AreaForm(Area area) {
            this.id = area.getId();
            this.city = area.getCity();
            this.district = area.getDistrict();
        }
    }
}
