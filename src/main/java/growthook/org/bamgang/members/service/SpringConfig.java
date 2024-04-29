package growthook.org.bamgang.members.service;

import growthook.org.bamgang.members.repository.DataFinishedWalkRepository;
import growthook.org.bamgang.members.repository.DataPickedWalkRepository;
import growthook.org.bamgang.members.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

    private final MemberRepository memberRepository;

    private final DataFinishedWalkRepository finishedWalkRepository;

    private final DataPickedWalkRepository dataPickedWalkRepository;

    @Autowired
    public SpringConfig(MemberRepository memberRepository, DataFinishedWalkRepository finishedWalkRepository, DataPickedWalkRepository dataPickedWalkRepository) {
        this.finishedWalkRepository = finishedWalkRepository;
        this.dataPickedWalkRepository = dataPickedWalkRepository;
        this.memberRepository = memberRepository;
    }

    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository,finishedWalkRepository, dataPickedWalkRepository);
    }
}