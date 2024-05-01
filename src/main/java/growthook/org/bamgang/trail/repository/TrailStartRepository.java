package growthook.org.bamgang.trail.repository;

import growthook.org.bamgang.trail.domain.TrailStart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrailStartRepository extends JpaRepository<TrailStart, Integer> {
    List<TrailStart> findTrailIdsByStartLatitude1BetweenAndStartLongitude1BetweenAndStartLatitude2BetweenAndStartLongitude2BetweenAndStartLatitude3BetweenAndStartLongitude3Between(
            Double minLatitude, Double maxLatitude,
            Double minLongitude, Double maxLongitude,
            Double minLatitude1, Double maxLatitude1,
            Double minLongitude1, Double maxLongitude1,
            Double minLatitude2, Double maxLatitude2,
            Double minLongitude2, Double maxLongitude2
    );
}
