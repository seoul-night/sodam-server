package growthook.org.bamgang.members.controller;

import growthook.org.bamgang.members.domain.FinishedWalk;
import growthook.org.bamgang.members.domain.Member;
import growthook.org.bamgang.members.domain.PickedWalk;
import growthook.org.bamgang.members.dto.request.FinishedWalkRequest;
import growthook.org.bamgang.members.dto.request.PickedWalkRequest;
import growthook.org.bamgang.members.dto.response.GetFinishedWalkResponseDto;
import growthook.org.bamgang.members.dto.response.GetMemberResponseDto;
import growthook.org.bamgang.members.dto.response.GetPickedWalkResponseDto;
import growthook.org.bamgang.members.service.MemberService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/members")
public class MemberController {

    @Autowired
    private final MemberService memberService;


    @Autowired
    public MemberController(final MemberService memberService) {
        this.memberService = memberService;
    }

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
}

