package bandwagon.bandwagonback.dto;

import bandwagon.bandwagonback.domain.*;
import bandwagon.bandwagonback.dto.subdto.AreaForm;
import bandwagon.bandwagonback.dto.subdto.BandMemberForm;
import bandwagon.bandwagonback.dto.subdto.IdNameForm;
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
}
