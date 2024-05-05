package growthook.org.bamgang.attractions.service;

import growthook.org.bamgang.attractions.domain.Attraction;
import growthook.org.bamgang.attractions.dto.response.GetAttractionPointDto;
import growthook.org.bamgang.attractions.dto.response.GetAttractionResponseDto;
import growthook.org.bamgang.attractions.repository.AttractionRepository;
import growthook.org.bamgang.members.dto.response.GetFinishedWalkResponseDto;
import growthook.org.bamgang.trail.dto.response.GetTrailResponseDto;
import growthook.org.bamgang.trail.repository.TrailStartRepository;
import growthook.org.bamgang.trail.service.TrailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AttractionService {


    public final AttractionRepository attractionRepository;

    public final TrailService trailService;

    @Autowired
    public AttractionService(AttractionRepository attractionRepository, TrailService trailService){
        this.attractionRepository = attractionRepository;
        this.trailService = trailService;
    }

    public List<GetAttractionResponseDto> selectAll() {
        List<Attraction> attractions = attractionRepository.findAll();

        List<GetAttractionResponseDto> list = new ArrayList<>();

        for(Attraction attraction : attractions){
            GetAttractionResponseDto dto = GetAttractionResponseDto.builder()
                    .attractionLatitude(attraction.getAttractionLatitude())
                    .attractionLongitude(attraction.getAttractionLongitude())
                    .attractionName(attraction.getAttractionName())
                    .attractionRegion(attraction.getAttractionRegion())
                    .attractionDetail(attraction.getAttractionDetail())
                    .attractionUrl(attraction.getAttractionUrl())
                    .build();
            list.add(dto);
        }

        return list;
    }

    public GetAttractionPointDto getAttractionPointDto(Double latitude, Double longitude) {
        List<GetTrailResponseDto> trailList = trailService.getNearAttractionTrail(latitude, longitude);

        if(trailList.isEmpty()){
            return null;
        }

        double minDistance = Double.MAX_VALUE;
        int minDistanceIndex = -1;

        // 주변 관광지 탐색
        for (int i = 0; i < trailList.size(); i++) {
            GetTrailResponseDto nearTrail = trailList.get(i);

            Double[] latitudeArray = nearTrail.getLatitudeList();
            Double[] longitudeArray = nearTrail.getLongitudeList();

            // 현재 관광지와의 최소 거리 초기화
            double curMinDistance = Double.MAX_VALUE;

            // 주변 관광지의 각 좌표에 대한 탐색
            for (int j = 0; j < latitudeArray.length; j++) {
                double nearLatitude = latitudeArray[j];
                double nearLongitude = longitudeArray[j];

                // 위도와 경도의 차이를 라디안으로 변환
                double latDiff = Math.toRadians(nearLatitude - latitude);
                double lonDiff = Math.toRadians(nearLongitude - longitude);

                // 하버사인 공식을 사용하여 거리 계산
                double a = Math.sin(latDiff / 2) * Math.sin(latDiff / 2) +
                        Math.cos(Math.toRadians(latitude)) * Math.cos(Math.toRadians(nearLatitude)) *
                                Math.sin(lonDiff / 2) * Math.sin(lonDiff / 2);

                double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

                // 거리 계산 (지구 반지름 6371 km)
                double distance = 6371 * c;

                // 최소 거리 갱신
                curMinDistance = Math.min(curMinDistance, distance);
            }

            // 현재 관광지의 최소 거리가 전체 최소 거리보다 작으면 갱신
            if (curMinDistance < minDistance) {
                minDistance = curMinDistance;
                minDistanceIndex = i;
            }
        }

        if (minDistanceIndex == -1) {
            return null; // 또는 예외 처리
        }
        Integer trailId = trailList.get(minDistanceIndex).getId();
        return GetAttractionPointDto.builder().trailId(trailId).build();
    }

}
