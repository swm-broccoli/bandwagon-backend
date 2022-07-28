package bandwagon.bandwagonback.dto.subdto;

import bandwagon.bandwagonback.domain.BandMember;
import bandwagon.bandwagonback.domain.Position;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BandMemberForm {

    private Long id;
    private String name;
    private List<IdNameForm> positions = new ArrayList<>();

    public BandMemberForm(BandMember bandMember) {
        this.id = bandMember.getId();
        this.name = bandMember.getMember().getName();
        for (Position position : bandMember.getPositions()) {
            this.positions.add(new IdNameForm(position));
        }
    }
}
