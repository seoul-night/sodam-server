package growthook.org.bamgang.trail.repository;

import growthook.org.bamgang.trail.domain.Trail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrailRepository extends JpaRepository<Trail, Long> {

}
