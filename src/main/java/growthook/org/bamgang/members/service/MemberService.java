package growthook.org.bamgang.members.service;

import growthook.org.bamgang.members.domain.FinishedWalk;
import growthook.org.bamgang.members.domain.Member;
import growthook.org.bamgang.members.domain.PickedWalk;
import growthook.org.bamgang.members.repository.DataFinishedWalkRepository;
import growthook.org.bamgang.members.repository.DataPickedWalkRepository;
import growthook.org.bamgang.members.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Transactional
@Service
public class MemberService {

    private MemberRepository memberRepository;

    private DataFinishedWalkRepository finishedWalkRepository;

    private DataPickedWalkRepository dataPickedWalkRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository, DataFinishedWalkRepository finishedWalkRepository, DataPickedWalkRepository dataPickedWalkRepository) {
        this.memberRepository = memberRepository;
        this.finishedWalkRepository = finishedWalkRepository;
        this.dataPickedWalkRepository = dataPickedWalkRepository;
    }

    // Memeber 조회
    public Member findById(int id) {
        return memberRepository.findByUserId(id);
    }

    // 완료한 산책로 조회
    public List<FinishedWalk> findFinishedWalkById(int id){
        return finishedWalkRepository.findAllByUserId(id);
    }

    // 완료한 산책로 추가
    public void saveFinishedWalk(FinishedWalk finishedWalk) {
        finishedWalkRepository.save(finishedWalk);
    }

    // 찜한 산책로 조회
    public List<PickedWalk> findPickedWalkById(int id){
        return dataPickedWalkRepository.findByUserId(id);
    }

    //산책로 찜하기
    public void savePickedWalk(int userId,int trailId) {
        PickedWalk pickedWalk = new PickedWalk();
        pickedWalk.setUserId(userId);
        pickedWalk.setTrailId(trailId);
        //조회 개발 완료시  추가
        //trailRepository.find
        pickedWalk.setTrailTitle("test_title1");
        pickedWalk.setTrailRegion("test_region1");
        dataPickedWalkRepository.save(pickedWalk);
    }
    
    //산책로 찜하기 취소
    public void deletePickedWalk(int userId,int trailId) {
        PickedWalk pickedWalk = dataPickedWalkRepository.findByUserIdAndTrailId(userId, trailId);
        dataPickedWalkRepository.delete(pickedWalk);
    }

}
