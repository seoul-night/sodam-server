package growthook.org.bamgang.members.service;

import growthook.org.bamgang.members.repository.DataFinishedWalkRepository;
import growthook.org.bamgang.members.repository.JpaMemberRepository;
import growthook.org.bamgang.members.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

    private final EntityManager em;

    private final DataFinishedWalkRepository finishedWalkRepository;

    @Autowired
    public SpringConfig(EntityManager em, DataFinishedWalkRepository finishedWalkRepository) {
        this.em = em;
        this.finishedWalkRepository = finishedWalkRepository;
    }

    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository() {
        return new JpaMemberRepository(em);
    }

    @Bean
    public FinishedWalkService finishedWalkService() {
        return new FinishedWalkService(finishedWalkRepository);
    }
}