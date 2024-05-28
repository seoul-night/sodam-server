package growthook.org.bamgang.members.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import growthook.org.bamgang.members.dto.request.FinishedWalkRequest;
import growthook.org.bamgang.members.dto.request.PickedWalkRequest;
import growthook.org.bamgang.members.dto.response.GetFinishedWalkResponseDto;
import growthook.org.bamgang.members.dto.response.GetMemberResponseDto;
import growthook.org.bamgang.members.dto.response.GetPickedWalkResponseDto;
import growthook.org.bamgang.members.dto.token.MemberToken;
import growthook.org.bamgang.members.jwtUtil.JWTUtil;
import growthook.org.bamgang.members.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    private final JWTUtil jwtUtil;

    @Value("${kakao-key}")
    private String apiKey;


    @GetMapping("/{id}")
    public GetMemberResponseDto getUserInfo(@PathVariable int id){
        GetMemberResponseDto getMemberResponseDto = memberService.findById(id);
        return getMemberResponseDto;
    }

    @GetMapping("/walks/complete/{id}")
    public List<GetFinishedWalkResponseDto> getUserInfo3(@PathVariable int id){
        List<GetFinishedWalkResponseDto> walks = memberService.findFinishedWalkById(id);
        return walks;
    }

    @PostMapping("/walks/complete")
    public void postUserInfo3(@RequestBody FinishedWalkRequest walk){
        memberService.saveFinishedWalk(walk);
    }

    @GetMapping("/walks/select/{id}")
    public List<GetPickedWalkResponseDto> getUserInfo4(@PathVariable int id){
        List<GetPickedWalkResponseDto> walks = memberService.findPickedWalkById(id);
        return walks;
    }

    @PostMapping("/walks/select")
    public void postUserInfo4(@RequestBody PickedWalkRequest pickedWalk){
        int userId = pickedWalk.getUserId();
        int trailId = pickedWalk.getTrailId();
        memberService.savePickedWalk(userId,trailId);
    }

    @DeleteMapping("/walks/select")
    public void deleteUserInfo4(@RequestBody PickedWalkRequest pickedWalk){
        int userId = pickedWalk.getUserId();
        int trailId = pickedWalk.getTrailId();
        memberService.deletePickedWalk(userId,trailId);
    }

    //kakao로그인
    @GetMapping("/kakao/login")
    public ResponseEntity<?> kakaoLogin()throws URISyntaxException {
        String redirectUri = "https://ddubam.site/api/members/kakao/oauth"; //배포
//        String redirectUri = "http://localhost:8080/api/members/kakao/oauth"; //테스트
        String kakaoAuthUri = "https://kauth.kakao.com/oauth/authorize?client_id=" + apiKey + "&redirect_uri=" + redirectUri + "&response_type=code";
        // 리다이렉트
        HttpHeaders httpHeaders = new HttpHeaders();
        URI redirectUriWithParams = new URI(kakaoAuthUri);
        httpHeaders.setLocation(redirectUriWithParams);
        return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
    }

    @GetMapping("/kakao/oauth")
    public ResponseEntity<?> kakao(@RequestParam("code") String code) throws URISyntaxException {
        String redirectUri = "https://ddubam.site/api/members/kakao/oauth"; // 배포
//        String redirectUri = "http://localhost:8080/api/members/kakao/oauth"; // 테스트
        String tokenUrl = "https://kauth.kakao.com/oauth/token";
        String userInfoUrl = "https://kapi.kakao.com/v2/user/me";

        // 요청으로 엑세스 토큰 꺼내기
        HttpHeaders tokenHeaders = new HttpHeaders();
        tokenHeaders.add("Content-Type", "application/x-www-form-urlencoded");

        MultiValueMap<String, String> tokenBody = new LinkedMultiValueMap<>();
        tokenBody.add("grant_type", "authorization_code");
        tokenBody.add("client_id", apiKey);
        tokenBody.add("redirect_uri", redirectUri);
        tokenBody.add("code", code);

        HttpEntity<MultiValueMap<String, String>> tokenRequestEntity = new HttpEntity<>(tokenBody, tokenHeaders);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> tokenResponse = restTemplate.exchange(tokenUrl, HttpMethod.POST, tokenRequestEntity, String.class);
        try {
            // 엑세스 토큰 꺼내기
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode tokenJsonNode = objectMapper.readTree(tokenResponse.getBody());
            String accessToken = tokenJsonNode.get("access_token").asText();

            // 엑세스 토큰으로 검색
            HttpHeaders userInfoHeaders = new HttpHeaders();
            userInfoHeaders.add("Authorization", "Bearer " + accessToken);
            userInfoHeaders.add("Content-Type", "application/x-www-form-urlencoded");

            MultiValueMap<String, String> userInfoBody = new LinkedMultiValueMap<>();
            userInfoBody.add("property_keys", "[\"kakao_account.profile\"]");

            HttpEntity<MultiValueMap<String, String>> userInfoRequestEntity = new HttpEntity<>(userInfoBody, userInfoHeaders);

            ResponseEntity<String> userInfoResponse = restTemplate.exchange(userInfoUrl, HttpMethod.POST, userInfoRequestEntity, String.class);

            // 결과값 꺼내오기
            JsonNode userInfoJsonNode = objectMapper.readTree(userInfoResponse.getBody());
            String  id = userInfoJsonNode.get("id").asText();
            String nickname = userInfoJsonNode.get("kakao_account").get("profile").get("nickname").asText();
            String profile = userInfoJsonNode.get("kakao_account").get("profile").get("profile_image_url").asText();

            MemberToken memberToken = memberService.createMember(id,profile,nickname);

            String token = jwtUtil.generateToken(memberToken);

            // 로그인
//            String redirectUrl = "https://ddubam.site/home?token="+token; // 배포
            String redirectUrl =  "http://localhost:3000/loading?token=" + token; //테스트
            URI redirectUriWithParams = new URI(redirectUrl);
//            System.out.println(token);
            // 리다이렉트
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setLocation(redirectUriWithParams);
            return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("로그인 실패");
        }
    }
}

