package bandwagon.bandwagonback.domain;

import bandwagon.bandwagonback.domain.prerequisite.PositionPrerequisite;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "positions")
@Getter @Setter
public class Position {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String position;

    @ManyToMany(mappedBy = "positions")
    private List<User> users = new ArrayList<>();

//    @ManyToMany(mappedBy = "positions")
//    private List<Band> bands = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_prerequisite_id")
    private PositionPrerequisite positionPrerequisite;

    @ManyToMany(mappedBy = "positions")
    private List<BandMember> bandMembers = new ArrayList<>();
}
