package growthook.org.bamgang.trail.repository;

import growthook.org.bamgang.trail.domain.TrailStart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrailStartRepository extends JpaRepository<TrailStart, Integer> {
    @Query("SELECT t.trailId FROM TrailStart t WHERE " +
            "((t.startLatitude1 BETWEEN :startLatitude1 AND :endLatitude1) AND (t.startLongitude1 BETWEEN :startLongitude1 AND :endLongitude1)) " +
            "OR ((t.startLatitude2 BETWEEN :startLatitude2 AND :endLatitude2) AND (t.startLongitude2 BETWEEN :startLongitude2 AND :endLongitude2)) " +
            "OR ((t.startLatitude3 BETWEEN :startLatitude3 AND :endLatitude3) AND (t.startLongitude3 BETWEEN :startLongitude3 AND :endLongitude3))")
    List<Integer> findTrailIdsByCoordinates(
            @Param("startLatitude1") Double startLatitude1, @Param("endLatitude1") Double endLatitude1,
            @Param("startLongitude1") Double startLongitude1, @Param("endLongitude1") Double endLongitude1,
            @Param("startLatitude2") Double startLatitude2, @Param("endLatitude2") Double endLatitude2,
            @Param("startLongitude2") Double startLongitude2, @Param("endLongitude2") Double endLongitude2,
            @Param("startLatitude3") Double startLatitude3, @Param("endLatitude3") Double endLatitude3,
            @Param("startLongitude3") Double startLongitude3, @Param("endLongitude3") Double endLongitude3);

}
