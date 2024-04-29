package growthook.org.bamgang.members.service;

import growthook.org.bamgang.members.repository.DataFinishedWalkRepository;
import growthook.org.bamgang.members.repository.DataPickedWalkRepository;
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

    private final DataPickedWalkRepository dataPickedWalkRepository;

    @Autowired
    public SpringConfig(EntityManager em, DataFinishedWalkRepository finishedWalkRepository, DataPickedWalkRepository dataPickedWalkRepository) {
        this.em = em;
        this.finishedWalkRepository = finishedWalkRepository;
        this.dataPickedWalkRepository = dataPickedWalkRepository;

    }

    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository(),finishedWalkRepository, dataPickedWalkRepository);
    }

    @Bean
    public MemberRepository memberRepository() {
        return new JpaMemberRepository(em);
    }
}