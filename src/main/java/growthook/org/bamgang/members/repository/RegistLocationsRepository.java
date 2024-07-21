package growthook.org.bamgang.members.repository;

import growthook.org.bamgang.members.domain.RegistLocations;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegistLocationsRepository extends JpaRepository<RegistLocations,Integer> {
    List<RegistLocations> findByUserIdOrderByIdDesc(int userId);
}
