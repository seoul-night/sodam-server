package growthook.org.bamgang.trail.repository;

import growthook.org.bamgang.trail.domain.Trail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrailRepository extends JpaRepository<Trail, Integer> {
    List<Trail> findTop10ByOrderByRatingDesc();

    @Query("SELECT MAX(id) FROM Trail")
    Integer findMaxTrailId();
}
