package growthook.org.bamgang.family.repository;

import growthook.org.bamgang.family.domain.FamilyLocations;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FamilyLocationsRepository extends JpaRepository<FamilyLocations, Integer> {
    List<FamilyLocations> findFamilyLocationsByMemberId(int memberId);
}
