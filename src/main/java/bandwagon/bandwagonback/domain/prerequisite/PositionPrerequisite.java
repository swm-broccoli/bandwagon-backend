package bandwagon.bandwagonback.domain.prerequisite;

import bandwagon.bandwagonback.domain.Genre;
import bandwagon.bandwagonback.domain.Position;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@DiscriminatorValue("Position")
public class PositionPrerequisite extends BandPrerequisite {

    @ManyToMany
    @JoinTable(name = "prerequisite_positions",
            joinColumns = @JoinColumn(name = "prerequisite_id"),
            inverseJoinColumns = @JoinColumn(name = "position_id"))
    private List<Position> positions = new ArrayList<>();

    // 지원 자격 포지션 추가
    public void addPosition(Position position) {
        if (!this.positions.contains(position)) {
            positions.add(position);
            position.getPositionPrerequisites().add(this);
        }
    }
    // 지원 자격 포지션 제거
    public void removePosition(Position position) {
        this.positions.remove(position);
        position.getPositionPrerequisites().remove(this);
    }

}
