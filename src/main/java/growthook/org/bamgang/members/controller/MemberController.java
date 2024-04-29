package growthook.org.bamgang.members.controller;

import growthook.org.bamgang.members.domain.FinishedWalk;
import growthook.org.bamgang.members.domain.Member;
import growthook.org.bamgang.members.service.FinishedWalkService;
import growthook.org.bamgang.members.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/members")
public class MemberController {

    @Autowired
    private final MemberService memberService;

    @Autowired
    private final FinishedWalkService finishedWalkService;

    @Autowired
    public MemberController(final MemberService memberService, final FinishedWalkService finishedWalkService) {
        this.memberService = memberService;
        this.finishedWalkService = finishedWalkService;
    }

    @GetMapping()
    public Optional<Member> getUserInfo(){
//        int id = data.getUserId();
        int id = 1;
        Optional<Member> member = memberService.findById(id);
        return member;
    }

    @GetMapping("/list3")
    public List<FinishedWalk> getUserInfo3(@RequestBody Member member){
        int id = member.getUserId();
        List<FinishedWalk> walks = finishedWalkService.findAllById(id);
        return walks;
    }

}

