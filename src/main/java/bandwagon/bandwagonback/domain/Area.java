package bandwagon.bandwagonback.domain;

import bandwagon.bandwagonback.domain.prerequisite.AreaPrerequisite;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "areas")
@Getter
@Setter
public class Area {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String city;

    private String district;

    @ManyToMany(mappedBy = "areas")
    private Set<User> users = new HashSet<>();

    @ManyToMany(mappedBy = "areas")
    private Set<Band> bands = new HashSet<>();

    @ManyToMany(mappedBy = "areas")
    private Set<AreaPrerequisite> areaPrerequisites = new HashSet<>();
}
