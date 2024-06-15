package growthook.org.bamgang.members.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import growthook.org.bamgang.members.domain.FinishedDestination;
import growthook.org.bamgang.members.domain.FinishedWalk;
import growthook.org.bamgang.members.domain.Member;
import growthook.org.bamgang.members.domain.PickedWalk;
import growthook.org.bamgang.members.dto.request.FinishedDestinationRequest;
import growthook.org.bamgang.members.dto.request.FinishedWalkRequest;
import growthook.org.bamgang.members.dto.response.GetFinishedDestinationResponseDto;
import growthook.org.bamgang.members.dto.response.GetFinishedWalkResponseDto;
import growthook.org.bamgang.members.dto.response.GetMemberResponseDto;
import growthook.org.bamgang.members.dto.response.GetPickedWalkResponseDto;
import growthook.org.bamgang.members.dto.token.MemberToken;
import growthook.org.bamgang.members.jwtUtil.JWTUtil;
import growthook.org.bamgang.members.repository.DataFinishedDestintationRepository;
import growthook.org.bamgang.members.repository.DataFinishedWalkRepository;
import growthook.org.bamgang.members.repository.DataPickedWalkRepository;
import growthook.org.bamgang.members.repository.MemberRepository;
import growthook.org.bamgang.trail.domain.Trail;
import growthook.org.bamgang.trail.repository.TrailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;


@Service
@Transactional(readOnly = true)
public class MemberService {

    private MemberRepository memberRepository;

    private DataFinishedWalkRepository finishedWalkRepository;

    private DataPickedWalkRepository dataPickedWalkRepository;

    public final TrailRepository trailRepository;

    private final DataFinishedDestintationRepository dataFinishedDestintationRepository;


    @Value("${kakao-key}")
    private String apiKey;

    @Autowired
    public MemberService(MemberRepository memberRepository, DataFinishedWalkRepository finishedWalkRepository, DataPickedWalkRepository dataPickedWalkRepository, TrailRepository trailRepository, DataFinishedDestintationRepository dataFinishedDestintationRepository) {
        this.memberRepository = memberRepository;
        this.finishedWalkRepository = finishedWalkRepository;
        this.dataPickedWalkRepository = dataPickedWalkRepository;
        this.trailRepository = trailRepository;
        this.dataFinishedDestintationRepository = dataFinishedDestintationRepository;
    }

    // Member 추가
    @Transactional
    public  MemberToken createMember(String passward,String profile,String nickname){
        Member findMember = memberRepository.findByPassword(passward);
        if(findMember!=null){
            MemberToken memberToken = new MemberToken();
            memberToken.setId(findMember.getUserId()+"");
            memberToken.setProfile(profile);
            memberToken.setNickName(nickname);
            return memberToken;
        }
        try {
            Member member = new Member();
            member.setNickName(nickname);
            member.setProfile(profile);
            member.setPassword(passward);
            member.setExp(0);
            member.setPickedCount(0);
            member.setFinishedCount(0);
            member.setWalkedDay(0);
            memberRepository.save(member);
        }catch (Exception e){
            e.printStackTrace();
        }
        findMember = memberRepository.findByPassword(passward);

        MemberToken memberToken = new MemberToken();
        memberToken.setId(findMember.getUserId()+"");
        memberToken.setProfile(profile);
        memberToken.setNickName(nickname);
        return memberToken;
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

    //Member 삭제
    @Transactional
    public void deleteMember(int id) {
        if (memberRepository.existsById(id)) {
            memberRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("ID가 " + id + "인 멤버가 존재하지 않습니다");
        }
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


    // 완료한 경로 후기 추가
    @Transactional
    public void saveFinishedDestination(FinishedDestinationRequest finishedDestinationRequest) {
        FinishedDestination finishedDestination = new FinishedDestination();
        int userId = finishedDestinationRequest.getUserId();
        finishedDestination.setUserId(userId);
        finishedDestination.setReview(finishedDestinationRequest.getReview());
        finishedDestination.setDestinationId(finishedDestinationRequest.getDestinationId());
        finishedDestination.setDestinationTitle(finishedDestinationRequest.getDestinationTitle());
        dataFinishedDestintationRepository.save(finishedDestination);

        // member 정보 업데이트
        Member member = memberRepository.findById(userId).orElseThrow(()->new RuntimeException());
        member.setFinishedCount(member.getFinishedCount() + 1);
    }

    // 완료한 경로 후기 조회
    public List<GetFinishedDestinationResponseDto> findFinishedSearchById(int id){
        List<FinishedDestination> finisheds = dataFinishedDestintationRepository.findAllByUserId(id);
        List<GetFinishedDestinationResponseDto> finishedDto = new ArrayList<>();
        for(FinishedDestination finished : finisheds){
            GetFinishedDestinationResponseDto finishedDestinationResponseDto = GetFinishedDestinationResponseDto.builder()
                    .destinationId(finished.getDestinationId())
                    .finishedDate(finished.getFinishedDate())
                    .destinationTitle(finished.getDestinationTitle())
                    .review(finished.getReview())
                    .finishedId(finished.getFinishedId())
                    .build();
            finishedDto.add(finishedDestinationResponseDto);
        }

        return finishedDto;
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
