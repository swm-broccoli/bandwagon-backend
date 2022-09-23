package bandwagon.bandwagonback.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "band_members")
@Getter @Setter
public class BandMember {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private User member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "band_id")
    private Band band;

    @ManyToMany
    @JoinTable(name = "band_member_positions",
            joinColumns = @JoinColumn(name = "band_member_id"),
            inverseJoinColumns = @JoinColumn(name = "position_id"))
    private Set<Position> positions = new HashSet<>();

    private Boolean isFrontman;

    public BandMember(){}

    // band는 Band entity의 addBandMember로 채워짐
    public BandMember(User user, Boolean isFrontman) {
        this.member = user;
        this.isFrontman = isFrontman;
    }

    //포지션 추가
    public void addPosition(Position position) {
        this.positions.add(position);
        position.getBandMembers().add(this);
    }

    //포지션 제거
    public void removePosition(Position position) {
        this.positions.remove(position);
        position.getBandMembers().remove(this);
    }
}
