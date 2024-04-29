package growthook.org.bamgang.members.repository;

import growthook.org.bamgang.members.domain.PickedWalk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DataPickedWalkRepository extends JpaRepository<PickedWalk, Integer> {

    List<PickedWalk> findByUserId(int userId);

}
