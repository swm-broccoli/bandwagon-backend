package bandwagon.bandwagonback.domain.post;

import bandwagon.bandwagonback.domain.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@DiscriminatorValue("User")
public class UserPost extends Post{

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}