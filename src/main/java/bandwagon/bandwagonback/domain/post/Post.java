package bandwagon.bandwagonback.domain.post;

import bandwagon.bandwagonback.domain.User;
import bandwagon.bandwagonback.dto.PostDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "posts")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter @Setter
@EntityListeners(AuditingEntityListener.class)
public abstract class Post {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    private String title;

    @Column(columnDefinition="TEXT")
    private String body;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(nullable = false, updatable = false)
    @LastModifiedDate
    private LocalDateTime updatedAt;

    private Integer view = 0;

    @Column(name = "dtype", insertable = false, updatable = false)
    private String dtype;

    @ManyToMany(mappedBy = "likedPosts")
    private Set<User> likingUsers = new HashSet<>();

    public Post() {}

    public Post(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public void update(PostDto postDto) {
        this.title = postDto.getTitle();
        this.body = postDto.getBody();
    }

    public int getLikeCount() {
        return likingUsers.size();
    }
}
