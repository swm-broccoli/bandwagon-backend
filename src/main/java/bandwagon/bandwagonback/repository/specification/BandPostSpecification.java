package bandwagon.bandwagonback.repository.specification;

import bandwagon.bandwagonback.domain.post.BandPost;
import org.springframework.data.jpa.domain.Specification;

public class BandPostSpecification {

    public static Specification<BandPost> containsStringInTitle(String title) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("title"), "%" + title + "%");
    }
}
