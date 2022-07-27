package bandwagon.bandwagonback.domain.post;

import bandwagon.bandwagonback.domain.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
@Getter @Setter
@DiscriminatorValue("User")
public class UserPost extends Post{

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
