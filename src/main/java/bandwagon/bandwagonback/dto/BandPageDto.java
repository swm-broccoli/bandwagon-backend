package bandwagon.bandwagonback.dto;

import bandwagon.bandwagonback.domain.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class BandPageDto {
    private Long id;
    private String name;
    private String avatarUrl;
    private List<BandMemberForm> bandMembers = new ArrayList<>();
    private List<AreaForm> areas = new ArrayList<>();
    private List<IdNameForm> genres = new ArrayList<>();
    private String description;
    private List<PerformanceDto> bandGigs = new ArrayList<>();
    private List<PerformanceDto> bandPractices = new ArrayList<>();
    private List<IdNameForm> bandPhotos = new ArrayList<>();

    public BandPageDto(Band band) {
        this.id = band.getId();
        this.name = band.getName();
        this.avatarUrl = band.getAvatarUrl();
        for (BandMember bandMember : band.getBandMembers()) {
            this.bandMembers.add(new BandMemberForm(bandMember));
        }
        for (Area area : band.getAreas()) {
            this.areas.add(new AreaForm(area));
        }
        for (Genre genre : band.getGenres()) {
            this.genres.add(new IdNameForm(genre));
        }
        this.description = band.getDescription();
        for (BandGig bandGig : band.getBandGigs()) {
            this.bandGigs.add(new PerformanceDto(bandGig));
        }
        for (BandPractice bandPractice : band.getBandPractices()) {
            this.bandPractices.add(new PerformanceDto(bandPractice));
        }
        for (BandPhoto bandPhoto : band.getBandPhotos()) {
            this.bandPhotos.add(new IdNameForm(bandPhoto));
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
        public IdNameForm(BandPhoto bandPhoto) {
            this.id = bandPhoto.getId();
            this.name = bandPhoto.getImgUrl();
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

    @Data
    static class BandMemberForm {
        private Long id;
        private String name;
        private List<IdNameForm> positions = new ArrayList<>();

        public BandMemberForm(BandMember bandMember) {
            this.id = bandMember.getId();
            this.name = bandMember.getMember().getName();
            for (Position position : bandMember.getPositions()) {
                this.positions.add(new IdNameForm(position));
            }
        }
    }
}
