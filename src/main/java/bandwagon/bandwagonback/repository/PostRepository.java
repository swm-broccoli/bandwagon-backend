package bandwagon.bandwagonback.repository;

import bandwagon.bandwagonback.domain.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long>{
    Page<Post> findAllByLikingUsers_email(String email, PageRequest pageRequest);
}
