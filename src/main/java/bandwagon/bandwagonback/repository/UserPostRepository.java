package bandwagon.bandwagonback.repository;

import bandwagon.bandwagonback.domain.post.UserPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPostRepository extends JpaRepository<UserPost, Long> {
}
