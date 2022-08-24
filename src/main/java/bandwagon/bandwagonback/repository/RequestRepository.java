package bandwagon.bandwagonback.repository;

import bandwagon.bandwagonback.domain.Band;
import bandwagon.bandwagonback.domain.Request;
import bandwagon.bandwagonback.domain.RequestType;
import bandwagon.bandwagonback.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    @Query("select distinct r from Request r left join fetch r.user left join fetch r.band left join fetch r.bandPost where r.user = :user and r.type = :type")
    List<Request> findAllByUserAndType(@Param("user") User user, @Param("type") RequestType type);
    @Query("select distinct r from Request r left join fetch r.user left join fetch r.band left join fetch r.bandPost where r.band = :band and r.type = :type")
    List<Request> findAllByBandAndType(@Param("band") Band band, @Param("type") RequestType type);
}
