package growthook.org.bamgang.members.repository;

import growthook.org.bamgang.members.domain.Member;

import java.util.Optional;

public interface MemberRepository {

    public Optional<Member> findByUserId(int userId);
}
