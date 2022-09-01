package bandwagon.bandwagonback.repository;

import bandwagon.bandwagonback.domain.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>{
    Page<Post> findAllByLikingUsers_email(String email, PageRequest pageRequest);

    @Query("select distinct p from Post p order by p.likingUsers.size desc")
    List<Post> findTop3OrderByLikingUsersSize();
}
