package bandwagon.bandwagonback.repository;

import bandwagon.bandwagonback.domain.Band;
import bandwagon.bandwagonback.domain.BandMember;
import bandwagon.bandwagonback.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BandMemberRepository extends JpaRepository<BandMember, Long> {
    public BandMember findFirstByMemberAndBand(User member, Band band);
    public BandMember findFirstByMember_emailAndBand_id(String email, Long bandId);
}
