package growthook.org.bamgang.members.repository;

import growthook.org.bamgang.members.domain.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Id;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public class JpaMemberRepository implements MemberRepository{

    private final EntityManager em;

    public JpaMemberRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Optional<Member> findByUserId(int userId) {
        Member member = em.find(Member.class, userId);
        return Optional.ofNullable(member);
    }


}
