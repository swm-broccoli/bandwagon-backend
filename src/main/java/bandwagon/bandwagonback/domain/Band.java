package bandwagon.bandwagonback.domain;

import bandwagon.bandwagonback.dto.BandCreateForm;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bands")
@Getter @Setter
public class Band {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "band_id")
    private Long id;

    private String name;

    @Column(columnDefinition="TEXT")
    private String description;

    @Column(columnDefinition="TEXT")
    private String avatarUrl;

    @ManyToMany
    @JoinTable(name = "band_positions",
            joinColumns = @JoinColumn(name = "band_id"),
            inverseJoinColumns = @JoinColumn(name = "position_id"))
    private List<Position> positions = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "band_genres",
            joinColumns = @JoinColumn(name = "band_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private List<Genre> genres = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "band_areas",
            joinColumns = @JoinColumn(name = "band_id"),
            inverseJoinColumns = @JoinColumn(name = "area_id"))
    private List<Area> areas = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "band")
    private List<BandMember> bandMembers = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "band")
    private List<BandGig> bandGigs = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "band")
    private List<BandPractice> bandPractices = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "band")
    private List<BandPhoto> bandPhotos = new ArrayList<>();

    public Band() {}

    public Band(BandCreateForm bandCreateForm) {
        this.name = bandCreateForm.getName();
        this.description = bandCreateForm.getDescription();
        this.avatarUrl = bandCreateForm.getAvatarUrl();
    }

    // 멤버 추가
    public void addBandMember(BandMember bandMember) {
        this.bandMembers.add(bandMember);
        bandMember.setBand(this);
    }

    // 공연기록 추가
    public void addBandGig(BandGig bandGig) {
        this.bandGigs.add(bandGig);
        bandGig.setBand(this);
    }
    // 합주기록 추가
    public void addBandPractice(BandPractice bandPractice) {
        this.bandPractices.add(bandPractice);
        bandPractice.setBand(this);
    }

    // 포지션 추가
    public void addPosition(Position position) {
        this.positions.add(position);
        position.getBands().add(this);
    }

    // 포지션 제거
    public void removePosition(Position position) {
        this.positions.remove(position);
        position.getBands().remove(this);
    }

    // 선호 장르 추가
    public void addGenre(Genre genre) {
        this.genres.add(genre);
        genre.getBands().add(this);
    }

    // 선호 장르 제거
    public void removeGenre(Genre genre) {
        this.genres.remove(genre);
        genre.getBands().remove(this);
    }

    // 활동 지역 추가
    public void addArea(Area area) {
        this.areas.add(area);
        area.getBands().add(this);
    }

    // 활동 지역 제거
    public void removeArea(Area area) {
        this.areas.remove(area);
        area.getBands().remove(this);
    }

    // 사진 추가
    public void addBandPhoto(BandPhoto bandPhoto) {
        this.bandPhotos.add(bandPhoto);
        bandPhoto.setBand(this);
    }
}
