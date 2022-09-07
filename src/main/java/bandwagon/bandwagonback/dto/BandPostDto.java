package bandwagon.bandwagonback.dto;

import bandwagon.bandwagonback.domain.User;
import bandwagon.bandwagonback.domain.post.BandPost;
import bandwagon.bandwagonback.domain.prerequisite.BandPrerequisite;
import bandwagon.bandwagonback.domain.prerequisite.PositionPrerequisite;
import bandwagon.bandwagonback.dto.subdto.IdNameForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = true)
public class BandPostDto extends PostDto{
    private String bandName;
    private String bandAvatarUrl;
    //TODO: 나중엔 필요한 테그 정보만 담게 변경
    private List<IdNameForm> tagInfo;

    public BandPostDto() {}

    public BandPostDto(BandPost post) {
        super(post);
        this.bandName = post.getBand().getName();
        this.bandAvatarUrl = post.getBand().getAvatarUrl();
        PositionPrerequisite positionPrereq = (PositionPrerequisite) post.getBandPrerequisites().stream().filter(prereq -> prereq.getDtype().equals("Position")).findFirst().orElse(null);
        if (positionPrereq != null) {
            this.tagInfo = positionPrereq.getPositions().stream().map(IdNameForm::new).sorted((a, b) -> (int) (a.getId() - b.getId())).limit(3).collect(Collectors.toList());
        } else {
            this.tagInfo = new ArrayList<>();
        }
    }

    public static BandPostDto makeBandPostDto(BandPost post, User user) {
        BandPostDto bandPostDto = new BandPostDto(post);
        if (user.getLikedPosts().contains(post)) {
            bandPostDto.setIsLiked(true);
        }
        return bandPostDto;
    }
}
