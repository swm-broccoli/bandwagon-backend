package bandwagon.bandwagonback.repository.specification;

import bandwagon.bandwagonback.domain.Band;
import bandwagon.bandwagonback.domain.Position;
import bandwagon.bandwagonback.domain.post.BandPost;
import bandwagon.bandwagonback.domain.prerequisite.BandPrerequisite;
import bandwagon.bandwagonback.domain.prerequisite.PositionPrerequisite;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.beans.Expression;
import java.util.*;
import java.util.stream.Collectors;

public class BandPostSpecification {

    public static Specification<BandPost> containsStringInTitle(String title) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("title"), "%" + title + "%");
    }

    public static Specification<BandPost> containsPosition(Integer[] positions) {
        List<Integer> newPositions = Arrays.asList(positions);
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            return criteriaBuilder.treat(root.join("bandPrerequisites"), PositionPrerequisite.class).join("positions").get("id").in(newPositions);
        };
    }
}
