package growthook.org.bamgang.members.repository;

import growthook.org.bamgang.members.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    public Member findByUserId(int userId);
}
