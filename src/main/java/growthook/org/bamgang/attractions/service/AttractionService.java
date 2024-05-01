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

    public GetAttractionPointDto getAttractionPointDto(double latitude, double longitude) {
        List<GetTrailResponseDto> trailList = trailService.getNearTrail(latitude,longitude);
        Integer trailId = trailList.get(0).getId();
        return GetAttractionPointDto.builder().trailId(trailId).build();
    }

}
