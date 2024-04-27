package growthook.org.bamgang.members.service;

import growthook.org.bamgang.members.domain.Member;
import growthook.org.bamgang.members.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Transactional
@Service
public class MemberService {

    private MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Optional<Member> findById(int id) {
        return memberRepository.findByUserId(id);
    }
}
