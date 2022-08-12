package bandwagon.bandwagonback.repository.specification;

import bandwagon.bandwagonback.domain.post.BandPost;
import bandwagon.bandwagonback.domain.prerequisite.AreaPrerequisite;
import bandwagon.bandwagonback.domain.prerequisite.GenrePrerequisite;
import bandwagon.bandwagonback.domain.prerequisite.PositionPrerequisite;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
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

    public static Specification<BandPost> containsArea(Integer[] areaIds) {
        List<Integer> areaIdList = Arrays.asList(areaIds);
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            return criteriaBuilder.treat(root.join("bandPrerequisites"), AreaPrerequisite.class).join("areas").get("id").in(areaIdList);
        };
    }

    public static Specification<BandPost> containsDay(Integer[] dayIds) {
        List<Integer> dayIdList = Arrays.asList(dayIds);
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            return (root.join("band").join("days").get("id").in(dayIdList));
        };
    }

    public static Specification<BandPost> ageGreaterThan(Integer minAge) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            return criteriaBuilder.lessThanOrEqualTo(criteriaBuilder.function("YEAR", Integer.class, root.join("band").join("bandMembers").get("member").get("birthday")),
                    LocalDate.now().minusYears(minAge - 1).getYear());
        };
    }
}
