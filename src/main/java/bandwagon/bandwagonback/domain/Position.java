package bandwagon.bandwagonback.domain;

import bandwagon.bandwagonback.domain.prerequisite.PositionPrerequisite;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "positions")
@Getter @Setter
public class Position {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String position;

    @ManyToMany(mappedBy = "positions")
    private Set<User> users = new HashSet<>();

    @ManyToMany(mappedBy = "positions")
    private List<PositionPrerequisite> positionPrerequisites = new ArrayList<>();

    @ManyToMany(mappedBy = "positions")
    private List<BandMember> bandMembers = new ArrayList<>();
}
