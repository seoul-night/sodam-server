package growthook.org.bamgang.members.service;

import growthook.org.bamgang.members.domain.*;
import growthook.org.bamgang.members.dto.request.AddFriendRequest;
import growthook.org.bamgang.members.dto.request.FinishedDestinationRequest;
import growthook.org.bamgang.members.dto.request.FinishedWalkRequest;
import growthook.org.bamgang.members.dto.request.RegistLocationsRequest;
import growthook.org.bamgang.members.dto.response.*;
import growthook.org.bamgang.members.dto.token.MemberToken;
import growthook.org.bamgang.members.exception.MemberNotFoundException;
import growthook.org.bamgang.members.repository.*;
import growthook.org.bamgang.trail.domain.Trail;
import growthook.org.bamgang.trail.repository.TrailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
public class MemberService {

    private static MemberRepository memberRepository;

    private DataFinishedWalkRepository finishedWalkRepository;

    private DataPickedWalkRepository dataPickedWalkRepository;

    public final TrailRepository trailRepository;

    private final DataFinishedDestintationRepository dataFinishedDestintationRepository;

    private final SearchWordRepository searchWordRepository;

    private final RegistLocationsRepository registLocationsRepository;


    @Value("${kakao-key}")
    private String apiKey;

    @Autowired
    public MemberService(MemberRepository memberRepository, DataFinishedWalkRepository finishedWalkRepository, DataPickedWalkRepository dataPickedWalkRepository, TrailRepository trailRepository, DataFinishedDestintationRepository dataFinishedDestintationRepository, SearchWordRepository searchWordRepository, RegistLocationsRepository registLocationsRepository) {
        this.memberRepository = memberRepository;
        this.finishedWalkRepository = finishedWalkRepository;
        this.dataPickedWalkRepository = dataPickedWalkRepository;
        this.trailRepository = trailRepository;
        this.dataFinishedDestintationRepository = dataFinishedDestintationRepository;
        this.searchWordRepository = searchWordRepository;
        this.registLocationsRepository = registLocationsRepository;
    }

    // Member 추가
    @Transactional
    public  MemberToken createMember(String passward,String profile,String nickname,String email){
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
            member.setEmail(email);
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
                .userId(member.getUserId())
                .nickName(member.getNickName())
                .exp(member.getExp())
                .finishedCount(member.getFinishedCount())
                .pickedCount(member.getPickedCount())
                .walkedDay(member.getWalkedDay())
                .profile(member.getProfile())
                .friendList(member.getFriendList())
                .email(member.getEmail())
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
        finishedDestination.setDestinaionLatitude(finishedDestinationRequest.getDestinaionLatitude());
        finishedDestination.setDestinaionLongitude(finishedDestinationRequest.getDestinaionLongitude());
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
                    .destinaionLatitude(finished.getDestinaionLatitude())
                    .destinaionLongitude(finished.getDestinaionLongitude())
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
    public void savePickedWalk(int userId,int trailId) {
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

    @Transactional
    // 최근 검색어 저장
    public void saveSearch(int userId, String word){
        Optional<SearchWord> existingSearchWord = searchWordRepository.findByUserIdAndWord(userId, word);

        if (existingSearchWord.isPresent()) {
            // 이미 존재하는 검색어일 경우, 검색 시간을 업데이트
            SearchWord searchWord = existingSearchWord.get();
            searchWord.setSearchTime(LocalDateTime.now());
            searchWordRepository.save(searchWord);
        } else {
            // 새로운 검색어일 경우, 새로 생성
            SearchWord searchWord = new SearchWord();
            searchWord.setWord(word);
            searchWord.setUserId(userId);
            searchWordRepository.save(searchWord);
        }
    }

    // 최근 검색어 조회
    public List<GetSearchWordResponseDto> getSearch(int userId){
        List<SearchWord> searchWords = searchWordRepository.getSearchWordsByUserIdOrderBySearchTimeDesc(userId);
        List<GetSearchWordResponseDto> searchList = new ArrayList<>();
        for(SearchWord searchWord : searchWords){
            GetSearchWordResponseDto dto = GetSearchWordResponseDto.builder()
                    .word(searchWord.getWord())
                    .id(searchWord.getId())
                    .build();
            searchList.add(dto);
        }
        return searchList;
    }

    @Transactional
    // 최근 검색어 삭제
    public void deleteSearch(int userId, int id){
        if(id == 0){
            searchWordRepository.deleteAllByUserId(userId);
        }else{
            searchWordRepository.deleteById(id);
        }
    }
  
    //등록 장소 조회
    public List<GetRegistLocationsResponseDto> getRegistLocations(int userId){
        List<RegistLocations> locations =registLocationsRepository.findByUserIdOrderByIdDesc(userId);
        List<GetRegistLocationsResponseDto> dtoList = new ArrayList<>();
        for(RegistLocations loc : locations){
            dtoList.add(GetRegistLocationsResponseDto.builder()
                            .address(loc.getAddress())
                            .name(loc.getName())
                            .latitude(loc.getLatitude())
                            .longitude(loc.getLongitude())
                            .id(loc.getId())
                            .build());
        }
        return dtoList;
    }

    // 장소 등록
    @Transactional
    public void postRegistLocations(RegistLocationsRequest requestBody){
        Member member = memberRepository.findById(requestBody.getUserId()).orElseThrow(()->new RuntimeException());
        member.setPickedCount(member.getPickedCount() + 1);

        RegistLocations registLocations = new RegistLocations();
        registLocations.setAddress(requestBody.getAddress());
        registLocations.setUserId(requestBody.getUserId());
        registLocations.setName(requestBody.getName());
        registLocations.setLongitude(requestBody.getLongitude());
        registLocations.setLatitude(requestBody.getLatitude());
        registLocationsRepository.save(registLocations);
    }

    // 장소 삭제
    @Transactional
    public void deleteRegistLocations(int id){
        RegistLocations deleteLocation = registLocationsRepository.findById(id).orElseThrow(()->new RuntimeException());
        Member member = memberRepository.findById(deleteLocation.getUserId()).orElseThrow(()->new RuntimeException());
        member.setPickedCount(member.getPickedCount() - 1);

        registLocationsRepository.deleteById(id);
    }

    // Email로 친구 찾기
    @Transactional(readOnly = true)
    public GetMemberResponseDto findByEmail(String targetUserEmail) {
        Member member = memberRepository.findByEmail(targetUserEmail)
                .orElseThrow(() -> new MemberNotFoundException("이메일이 " + targetUserEmail + " 인 유저를 찾지 못했습니다."));
        return convertToDto(member);
    }

    // 친구 Id로 친구 추가
    @Transactional
    public GetMemberResponseDto addFriendById(int userId, AddFriendRequest friendRequest) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new MemberNotFoundException("아이디가 " + userId + " 인 유저가 없습니다."));
        Member friend = memberRepository.findById(friendRequest.getFriendId())
                .orElseThrow(() -> new MemberNotFoundException("아이디가 " + friendRequest.getFriendId() + " 인 친구가 없습니다."));

        List<Integer> friendList = new ArrayList<>(Arrays.asList(member.getFriendList()));
        if(!friendList.contains(friend.getUserId())){
            friendList.add(friend.getUserId());
            member.setFinishedCount(member.getFinishedCount()+1);
        }

        member.setFriendList(friendList.toArray(new Integer[friendList.size()]));
        memberRepository.save(member);

        return convertToDto(member);
    }

    // 친구 정보 가져오기
    @Transactional(readOnly = true)
    public List<GetMemberResponseDto> getFriendsInfo(int userId) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new MemberNotFoundException("아이디가 " + userId + " 인 유저가 없습니다."));

        if (member.getFriendList() == null) {
            throw new MemberNotFoundException("검색된 사용자가 없습니다.");
        }

        List<GetMemberResponseDto> friendInfoList = new ArrayList<>();
        for(Integer friendId : member.getFriendList()){
            Member friend = memberRepository.findById(friendId)
                    .orElseThrow(() -> new MemberNotFoundException("아이디가 " + friendId + " 인 친구가 없습니다."));
            friendInfoList.add(convertToDto(friend));
        }

        return friendInfoList;
    }

    // 친구 관계 삭제
    @Transactional
    public void removeFriend(int userId, int friendId) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new MemberNotFoundException("아이디가 " + userId + " 인 유저가 없습니다."));

        List<Integer> friendList = new ArrayList<>(Arrays.asList(member.getFriendList()));
        friendList.remove((Integer) friendId);
        member.setFinishedCount(member.getFinishedCount()-1);
        member.setFriendList(friendList.toArray(new Integer[friendList.size()]));
        memberRepository.save(member);
    }

    private GetMemberResponseDto convertToDto(Member member) {
        return GetMemberResponseDto.builder()
                .userId(member.getUserId())
                .nickName(member.getNickName())
                .exp(member.getExp())
                .finishedCount(member.getFinishedCount())
                .pickedCount(member.getPickedCount())
                .walkedDay(member.getWalkedDay())
                .profile(member.getProfile())
                .friendList(member.getFriendList())
                .email(member.getEmail())
                .build();
    }

}
