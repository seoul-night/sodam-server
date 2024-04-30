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

    @Autowired
    public MemberService(MemberRepository memberRepository, DataFinishedWalkRepository finishedWalkRepository, DataPickedWalkRepository dataPickedWalkRepository) {
        this.memberRepository = memberRepository;
        this.finishedWalkRepository = finishedWalkRepository;
        this.dataPickedWalkRepository = dataPickedWalkRepository;
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
        finishedWalk.setUserId(finishedWalkRequest.getUserId());
        finishedWalk.setReview(finishedWalkRequest.getReview());
        finishedWalk.setTrailId(finishedWalkRequest.getTrailId());
        // 조회하면 title
        finishedWalk.setTrailTitle("title");
        finishedWalkRepository.save(finishedWalk);
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
    public void savePickedWalk(int userId,int trailId) {
        // member 정보 업데이트
        Member member = memberRepository.findById(userId).orElseThrow(()->new RuntimeException());
        member.setPickedCount(member.getPickedCount() + 1);

        // 찜하기 목록에 추가
        PickedWalk pickedWalk = new PickedWalk();
        pickedWalk.setUserId(userId);
        pickedWalk.setTrailId(trailId);
        //조회 개발 완료시  추가
        //trailRepository.find
        pickedWalk.setTrailTitle("test_title1");
        pickedWalk.setTrailRegion("test_region1");

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
