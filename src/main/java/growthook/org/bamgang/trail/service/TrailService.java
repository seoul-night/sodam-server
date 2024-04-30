package growthook.org.bamgang.trail.service;

import growthook.org.bamgang.trail.dto.response.GetTrailResponseDto;

public interface TrailService {

    public GetTrailResponseDto getTrailById(Integer trailId);
}
