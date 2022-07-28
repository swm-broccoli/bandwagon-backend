package bandwagon.bandwagonback.domain;

import bandwagon.bandwagonback.domain.prerequisite.AreaPrerequisite;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
    private List<User> users = new ArrayList<>();

    @ManyToMany(mappedBy = "areas")
    private List<Band> bands = new ArrayList<>();

    @ManyToMany(mappedBy = "areas")
    private List<AreaPrerequisite> areaPrerequisites = new ArrayList<>();
}
