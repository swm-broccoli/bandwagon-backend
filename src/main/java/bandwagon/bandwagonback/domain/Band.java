package bandwagon.bandwagonback.domain;

import bandwagon.bandwagonback.domain.post.BandPost;
import bandwagon.bandwagonback.dto.BandCreateForm;
import bandwagon.bandwagonback.listener.BandEntityListener;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "bands")
@Getter @Setter
@EntityListeners(BandEntityListener.class)
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
    @JoinTable(name = "band_genres",
            joinColumns = @JoinColumn(name = "band_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private Set<Genre> genres = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "band_areas",
            joinColumns = @JoinColumn(name = "band_id"),
            inverseJoinColumns = @JoinColumn(name = "area_id"))
    private Set<Area> areas = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "band_days",
            joinColumns = @JoinColumn(name = "band_id"),
            inverseJoinColumns = @JoinColumn(name = "day_id"))
    private Set<Day> days = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "band", cascade = CascadeType.REMOVE)
    private List<BandMember> bandMembers = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "band", cascade = CascadeType.REMOVE)
    private List<BandGig> bandGigs = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "band", cascade = CascadeType.REMOVE)
    private List<BandPractice> bandPractices = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "band", cascade = CascadeType.REMOVE)
    private List<BandPhoto> bandPhotos = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "band", cascade = CascadeType.REMOVE)
    private List<BandPost> bandPosts = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "band")
    private List<Request> requests = new ArrayList<>();

    public Band() {}

    public Band(BandCreateForm bandCreateForm) {
        this.name = bandCreateForm.getName();
        this.description = bandCreateForm.getDescription();
        this.avatarUrl = bandCreateForm.getAvatarUrl();
    }

    // 밴드 최소 나이 추출
    public int getBandMemberMinAge() {
        return bandMembers.stream().mapToInt(v -> v.getMember().getUserAge()).min().orElse(Integer.MIN_VALUE);
    }

    // 밴드 최대 나이 추출
    public int getBandMemberMaxAge() {
        return bandMembers.stream().mapToInt(v -> v.getMember().getUserAge()).max().orElse(Integer.MAX_VALUE);
    }

    // 멤버 추가
    public void addBandMember(BandMember bandMember) {
        this.bandMembers.add(bandMember);
        bandMember.setBand(this);
    }

    public void removeBandMember(BandMember bandMember) {
        this.bandMembers.remove(bandMember);
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

    // 활동 요일 추가
    public void addDay(Day day) {
        this.days.add(day);
        day.getBands().add(this);
    }

    // 활동 요일 제거
    public void removeDay(Day day) {
        this.days.remove(day);
        day.getBands().remove(this);
    }

    // 게시글 등록
    public void addPost(BandPost post) {
        this.bandPosts.add(post);
        post.setBand(this);
    }

    // 요청 추가
    public void addRequest(Request request) {
        this.getRequests().add(request);
        request.setBand(this);
    }
}
