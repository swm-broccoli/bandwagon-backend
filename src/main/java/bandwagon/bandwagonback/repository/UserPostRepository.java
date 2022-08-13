package bandwagon.bandwagonback.repository;

import bandwagon.bandwagonback.domain.User;
import bandwagon.bandwagonback.domain.post.UserPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UserPostRepository extends JpaRepository<UserPost, Long>, JpaSpecificationExecutor<UserPost> {
    boolean existsByUser(User user);
}
