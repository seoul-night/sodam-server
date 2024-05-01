package growthook.org.bamgang.trail.service;

import growthook.org.bamgang.trail.domain.Trail;
import growthook.org.bamgang.trail.dto.response.GetTrailResponseDto;

import java.util.List;

public interface TrailService {

    // 산책로 상세정보 조회
    public GetTrailResponseDto getTrailById(Integer trailId);

    // 주변 산책로 조회
    List<GetTrailResponseDto> getNearTrail(Double latitude, Double longitude);

    GetTrailResponseDto convertToResponseDto(Trail trail);
}
