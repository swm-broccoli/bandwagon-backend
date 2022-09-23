package bandwagon.bandwagonback.domain.post;

import bandwagon.bandwagonback.domain.Band;
import bandwagon.bandwagonback.domain.Request;
import bandwagon.bandwagonback.domain.prerequisite.BandPrerequisite;
import bandwagon.bandwagonback.dto.PostDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@DiscriminatorValue("Band")
public class BandPost extends Post{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "band_id")
    private Band band;

    @OneToMany(mappedBy = "bandPost", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<BandPrerequisite> bandPrerequisites = new ArrayList<>();

    @OneToMany(mappedBy = "bandPost", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Request> requests = new ArrayList<>();

    public BandPost() {}

    public BandPost(PostDto postDto) {
        super(postDto.getTitle(), postDto.getBody());
    }

    // Prerequisite 추가
    public void addPrerequisite(BandPrerequisite bandPrerequisite) {
        if (!this.bandPrerequisites.contains(bandPrerequisite)) {
            this.bandPrerequisites.add(bandPrerequisite);
            bandPrerequisite.setBandPost(this);
        }
    }

    // 해당 게시글을 통한 가입 신청 추가
    public void addRequest(Request request) {
        this.requests.add(request);
        request.setBandPost(this);
    }
}
