package growthook.org.bamgang.trail.service;

import growthook.org.bamgang.trail.domain.Trail;
import growthook.org.bamgang.trail.dto.response.GetTrailResponseDto;
import growthook.org.bamgang.trail.repository.TrailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrailServiceImpl implements TrailService{

    public final TrailRepository trailRepository;

    @Autowired
    public TrailServiceImpl(TrailRepository trailRepository) {
        this.trailRepository = trailRepository;
    }
    public GetTrailResponseDto getTrailById(Integer trailId) {
        Trail trail = trailRepository.findById(trailId)
                .orElseThrow(() -> new RuntimeException("Trail not found with id: " + trailId));
        // Domain 객체를 Response DTO로 변환
        return GetTrailResponseDto.builder()
                .id(trail.getId())
                .title(trail.getTitle())
                .detail(trail.getDetail())
                .image(trail.getImage())
                .distance(trail.getDistance())
                .cctvArr(trail.getCctvArr())
                .rating(trail.getRating())
                .time(trail.getTime())
                .level(trail.getLevel())
                .region(trail.getRegion())
                .build();
    }
}
