package bandwagon.bandwagonback.repository;

import bandwagon.bandwagonback.domain.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserInfoRepository {

    private final EntityManager em;

    public void save(UserInfo userInfo) {
        em.persist(userInfo);
    }

    public UserInfo findOne(Long id) {
        return em.find(UserInfo.class, id);
    }

    public List<UserInfo> findAll() {
        return em.createQuery("select u from UserInfo u", UserInfo.class)
                .getResultList();
    }
}
