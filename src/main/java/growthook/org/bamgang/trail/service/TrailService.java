package growthook.org.bamgang.trail.service;

import growthook.org.bamgang.trail.repository.TrailRepository;
import org.springframework.stereotype.Service;

@Service

public interface TrailService {
    public final TrailRepository trailRepository;

    @Autowired
    public TrailService(TrailRepository trailRepository) {
        this.trailRepository = trailRepository;
    }

    public TrailResponseDto getTrailById(Integer trailId) {
        Trail trail = trailRepository.findById(trailId)
                .orElseThrow(() -> new RuntimeException("Trail not found with id: " + trailId));
        // Domain 객체를 Response DTO로 변환
        return new TrailResponseDto(trail);
    }
}
