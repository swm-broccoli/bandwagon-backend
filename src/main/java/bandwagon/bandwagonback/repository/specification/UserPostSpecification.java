package bandwagon.bandwagonback.repository.specification;

import bandwagon.bandwagonback.domain.post.BandPost;
import bandwagon.bandwagonback.domain.post.UserPost;
import bandwagon.bandwagonback.domain.prerequisite.PositionPrerequisite;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class UserPostSpecification {

    public static Specification<UserPost> containsStringInTitle(String title) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("title"), "%" + title + "%");
    }

    public static Specification<UserPost> isGender(Boolean gender) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("user").get("gender"), gender);
    }

    public static Specification<UserPost> playsPosition(Integer[] positionIds) {
        List<Integer> positionIdList = Arrays.asList(positionIds);
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            return root.join("user").join("positions").get("id").in(positionIdList);
        };
    }

    public static Specification<UserPost> likesGenre(Integer[] genreIds) {
        List<Integer> genreIdList = Arrays.asList(genreIds);
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            return root.join("user").join("genres").get("id").in(genreIdList);
        };
    }

    public static Specification<UserPost> availableArea(Integer[] areaIds) {
        List<Integer> areaIdList = Arrays.asList(areaIds);
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            return root.join("user").join("areas").get("id").in(areaIdList);
        };
    }

    public static Specification<UserPost> ageGreaterThan(Integer minAge) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            return criteriaBuilder.lessThanOrEqualTo(criteriaBuilder.function("YEAR", Integer.class, root.join("user").get("birthday")),
                    LocalDate.now().minusYears(minAge - 1).getYear());
        };
    }

    public static Specification<UserPost> ageLessThan(Integer maxAge) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            return criteriaBuilder.greaterThanOrEqualTo(criteriaBuilder.function("YEAR", Integer.class, root.join("user").get("birthday")),
                    LocalDate.now().minusYears(maxAge - 1).getYear());
        };
    }
}
