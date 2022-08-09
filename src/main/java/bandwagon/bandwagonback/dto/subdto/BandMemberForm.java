package bandwagon.bandwagonback.dto.subdto;

import bandwagon.bandwagonback.domain.BandMember;
import bandwagon.bandwagonback.domain.Position;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class BandMemberForm {

    private Long id;
    private String name;
    private LocalDate birthday;
    private List<IdNameForm> positions = new ArrayList<>();
    private Boolean isFrontman;

    public BandMemberForm(BandMember bandMember) {
        this.id = bandMember.getId();
        this.name = bandMember.getMember().getName();
        this.birthday = bandMember.getMember().getBirthday();
        for (Position position : bandMember.getPositions()) {
            this.positions.add(new IdNameForm(position));
        }
        this.isFrontman = bandMember.getIsFrontman();
    }
}
