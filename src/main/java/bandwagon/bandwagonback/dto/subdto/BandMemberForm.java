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

    private String avatarUrl;
    private Long id;
    private String name;
    private String nickname;
    private String email;
    private LocalDate birthday;
    private Integer age;
    private List<IdNameForm> positions = new ArrayList<>();
    private Boolean isFrontman;

    public BandMemberForm(BandMember bandMember) {
        this.avatarUrl = bandMember.getMember().getUserInfo().getAvatarUrl();
        this.id = bandMember.getId();
        this.name = bandMember.getMember().getName();
        this.nickname = bandMember.getMember().getNickname();
        this.email = bandMember.getMember().getEmail();
        this.birthday = bandMember.getMember().getBirthday();
        this.age = bandMember.getMember().getUserAge();
        for (Position position : bandMember.getPositions()) {
            this.positions.add(new IdNameForm(position));
        }
        this.isFrontman = bandMember.getIsFrontman();
    }
}
