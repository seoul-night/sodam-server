package growthook.org.bamgang.trail.controller;

import growthook.org.bamgang.trail.domain.Trail;
import growthook.org.bamgang.trail.dto.response.GetTrailResponseDto;
import growthook.org.bamgang.trail.service.TrailService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/walks")
public class TrailController {
    private final TrailService trailService;
    public TrailController(TrailService trailService) {
        this.trailService = trailService;
    }
    @GetMapping("/{trailId}")
    public GetTrailResponseDto getTrailById(@PathVariable("trailId") Integer trailId){
        return trailService.getTrailById(trailId);
    }

}
