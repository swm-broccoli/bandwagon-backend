package bandwagon.bandwagonback.domain;

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
}
