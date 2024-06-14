package growthook.org.bamgang.attractions.repository;

import growthook.org.bamgang.attractions.domain.Attraction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttractionRepository extends JpaRepository<Attraction, Integer> {
    @Query(value = "SELECT * FROM attraction a " +
            "WHERE a.attraction_id IN (" +
            "    SELECT MIN(attraction_id) " +
            "    FROM attraction " +
            "    GROUP BY attraction_latitude, attraction_longitude" +
            ") " +
            "ORDER BY " +
            "6371 * acos(cos(radians(:latitude)) * cos(radians(CAST(a.attraction_latitude AS double precision))) " +
            "* cos(radians(CAST(a.attraction_longitude AS double precision)) - radians(:longitude)) " +
            "+ sin(radians(:latitude)) * sin(radians(CAST(a.attraction_latitude AS double precision)))) " +
            "LIMIT 10",
            nativeQuery = true)
    List<Attraction> findNearestAttractions(@Param("latitude") double latitude, @Param("longitude") double longitude);
}

