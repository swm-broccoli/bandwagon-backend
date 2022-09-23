package bandwagon.bandwagonback.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "days")
@Getter @Setter
public class Day {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String day;

    @ManyToMany(mappedBy = "days")
    private Set<Band> bands = new HashSet<>();
}
