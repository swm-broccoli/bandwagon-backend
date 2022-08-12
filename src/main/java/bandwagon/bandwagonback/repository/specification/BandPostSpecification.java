package bandwagon.bandwagonback.repository.specification;

import bandwagon.bandwagonback.domain.post.BandPost;
import bandwagon.bandwagonback.domain.prerequisite.GenrePrerequisite;
import bandwagon.bandwagonback.domain.prerequisite.PositionPrerequisite;
import org.springframework.data.jpa.domain.Specification;
import java.util.*;

public class BandPostSpecification {

    public static Specification<BandPost> containsStringInTitle(String title) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("title"), "%" + title + "%");
    }

    public static Specification<BandPost> containsPosition(Integer[] positionIds) {
        List<Integer> positionIdList = Arrays.asList(positionIds);
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            return criteriaBuilder.treat(root.join("bandPrerequisites"), PositionPrerequisite.class).join("positions").get("id").in(positionIdList);
        };
    }

    public static Specification<BandPost> containsGenre(Integer[] genreIds) {
        List<Integer> genreIdList = Arrays.asList(genreIds);
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            return criteriaBuilder.treat(root.join("bandPrerequisites"), GenrePrerequisite.class).join("genres").get("id").in(genreIdList);
        };
    }
}
