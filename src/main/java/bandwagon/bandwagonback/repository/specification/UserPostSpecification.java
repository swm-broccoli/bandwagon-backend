package bandwagon.bandwagonback.repository.specification;

import bandwagon.bandwagonback.domain.post.BandPost;
import bandwagon.bandwagonback.domain.post.UserPost;
import org.springframework.data.jpa.domain.Specification;

public class UserPostSpecification {

    public static Specification<UserPost> containsStringInTitle(String title) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("title"), "%" + title + "%");
    }

    public static Specification<UserPost> isGender(Boolean gender) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("user").get("gender"), gender);
    }
}
