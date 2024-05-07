package growthook.org.bamgang.members.service;

import growthook.org.bamgang.members.domain.FinishedWalk;
import growthook.org.bamgang.members.domain.Member;
import growthook.org.bamgang.members.domain.PickedWalk;
import growthook.org.bamgang.members.dto.request.FinishedWalkRequest;
import growthook.org.bamgang.members.dto.response.GetFinishedWalkResponseDto;
import growthook.org.bamgang.members.dto.response.GetMemberResponseDto;
import growthook.org.bamgang.members.dto.response.GetPickedWalkResponseDto;
import growthook.org.bamgang.members.repository.DataFinishedWalkRepository;
import growthook.org.bamgang.members.repository.DataPickedWalkRepository;
import growthook.org.bamgang.members.repository.MemberRepository;
import growthook.org.bamgang.trail.domain.Trail;
import growthook.org.bamgang.trail.repository.TrailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
public class MemberService {

    private MemberRepository memberRepository;

    private DataFinishedWalkRepository finishedWalkRepository;

    private DataPickedWalkRepository dataPickedWalkRepository;

    public final TrailRepository trailRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository, DataFinishedWalkRepository finishedWalkRepository, DataPickedWalkRepository dataPickedWalkRepository, TrailRepository trailRepository) {
        this.memberRepository = memberRepository;
        this.finishedWalkRepository = finishedWalkRepository;
        this.dataPickedWalkRepository = dataPickedWalkRepository;
        this.trailRepository = trailRepository;
    }

    // Memeber 조회
    public GetMemberResponseDto findById(int id) {
        Member member = memberRepository.findByUserId(id).orElseThrow(()-> new RuntimeException());
        GetMemberResponseDto getMemberResponseDto = GetMemberResponseDto.builder()
                .nickName(member.getNickName())
                .exp(member.getExp())
                .finishedCount(member.getFinishedCount())
                .pickedCount(member.getPickedCount())
                .walkedDay(member.getWalkedDay())
                .profile(member.getProfile())
                .build();
        return getMemberResponseDto;
    }

    // 완료한 산책로 조회
    public List<GetFinishedWalkResponseDto> findFinishedWalkById(int id){
        List<FinishedWalk> walks = finishedWalkRepository.findAllByUserId(id);
        List<GetFinishedWalkResponseDto> walksDto = new ArrayList<>();
        for(FinishedWalk walk : walks){
            GetFinishedWalkResponseDto walkDto = GetFinishedWalkResponseDto.builder()
                    .trailId(walk.getTrailId())
                    .trailTitle(walk.getTrailTitle())
                    .walkedDate(walk.getWalkedDate())
                    .review(walk.getReview())
                    .build();
            walksDto.add(walkDto);
        }

        return walksDto;
    }

    // 완료한 산책로 추가
    @Transactional
    public void saveFinishedWalk(FinishedWalkRequest finishedWalkRequest) {
        FinishedWalk finishedWalk = new FinishedWalk();
        int userId = finishedWalkRequest.getUserId();
        finishedWalk.setUserId(userId);
        System.out.println(userId);
        finishedWalk.setReview(finishedWalkRequest.getReview());
        int trailId = finishedWalkRequest.getTrailId();
        finishedWalk.setTrailId(trailId);
        // trail의 정보를 조회해서 추가
        Trail trail = trailRepository.findById(trailId).orElseThrow(()->new RuntimeException());
        finishedWalk.setTrailTitle(trail.getTitle());
        finishedWalkRepository.save(finishedWalk);

        // member 정보 업데이트
        Member member = memberRepository.findById(userId).orElseThrow(()->new RuntimeException());
        member.setFinishedCount(member.getFinishedCount() + 1);
    }

    // 찜한 산책로 조회
    public List<GetPickedWalkResponseDto> findPickedWalkById(int id){
        List<PickedWalk> walks = dataPickedWalkRepository.findByUserId(id);
        List<GetPickedWalkResponseDto> walksDto = new ArrayList<>();
        for(PickedWalk walk : walks){
            GetPickedWalkResponseDto walkDto = GetPickedWalkResponseDto.builder()
                    .trailId(walk.getTrailId())
                    .trailTitle(walk.getTrailTitle())
                    .trailRegion(walk.getTrailRegion())
                    .build();
            walksDto.add(walkDto);
        }
        return walksDto;
    }

    //산책로 찜하기
    @Transactional
    public void     savePickedWalk(int userId,int trailId) {
        // member 정보 업데이트
        Member member = memberRepository.findById(userId).orElseThrow(()->new RuntimeException());
        member.setPickedCount(member.getPickedCount() + 1);

        // 찜하기 목록에 추가
        PickedWalk pickedWalk = new PickedWalk();
        pickedWalk.setUserId(userId);
        pickedWalk.setTrailId(trailId);
        // trail의 정보를 조회해서 추가
        Trail trail = trailRepository.findById(trailId).orElseThrow(()->new RuntimeException());
        pickedWalk.setTrailTitle(trail.getTitle());
        pickedWalk.setTrailRegion(trail.getRegion());

        dataPickedWalkRepository.save(pickedWalk);
    }

    @Transactional
    //산책로 찜하기 취소
    public void deletePickedWalk(int userId,int trailId) {
        //member 업데이트
        Member member = memberRepository.findByUserId(userId).orElseThrow(()->new RuntimeException());
        member.setPickedCount(member.getPickedCount() - 1);

        // 찜한 산책로 제거
        PickedWalk pickedWalk = dataPickedWalkRepository.findByUserIdAndTrailId(userId, trailId);
        dataPickedWalkRepository.delete(pickedWalk);
    }

}
