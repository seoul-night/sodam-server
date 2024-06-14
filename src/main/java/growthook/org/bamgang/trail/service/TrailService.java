package growthook.org.bamgang.trail.service;

import growthook.org.bamgang.trail.domain.Trail;
import growthook.org.bamgang.trail.dto.response.GetNewTrailResponseDto;
import growthook.org.bamgang.trail.dto.response.GetPopularTrailResponseDto;
import growthook.org.bamgang.trail.dto.response.GetSearchTrailResponseDto;
import growthook.org.bamgang.trail.dto.response.GetTrailResponseDto;

import java.util.List;

public interface TrailService {

    // 산책로 상세정보 조회
    public GetTrailResponseDto getTrailById(Integer trailId, Integer userId);

    // 주변 산책로 조회
    List<GetTrailResponseDto> getNearTrail(Double latitude, Double longitude);

    GetTrailResponseDto convertToResponseDto(Trail trail);

    // 주변 산책로 조회
    List<GetTrailResponseDto> getNearAttractionTrail(Double latitude, Double longitude);

    List<GetTrailResponseDto> getPopularTrail();

    // 산책로 생성
    List<GetNewTrailResponseDto> getNewTrail(Double latitude, Double longitude);

    //경로 생성
    GetSearchTrailResponseDto getSearchTrail(Double startLatitude, Double startLongitude, Double endLatitude, Double endLongitude);

    // 인기 산책로 경로 조회
    GetPopularTrailResponseDto getPopularTrail(Integer trailId, Integer userId, Double latitude, Double longitude);
}
