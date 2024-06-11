package growthook.org.bamgang.trail.repository;

import growthook.org.bamgang.trail.domain.Safety;
import growthook.org.bamgang.trail.domain.Trail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SafetyRepository extends JpaRepository<Safety, Integer> {

}
