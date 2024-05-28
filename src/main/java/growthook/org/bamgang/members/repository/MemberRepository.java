package growthook.org.bamgang.members.repository;

import growthook.org.bamgang.members.domain.Member;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Integer> {

    Optional<Member> findByUserId(int userId);

    Member findByPassword(String password);
}
