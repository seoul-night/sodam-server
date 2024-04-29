package growthook.org.bamgang.members.repository;

import growthook.org.bamgang.members.domain.FinishedWalk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface DataFinishedWalkRepository extends JpaRepository<FinishedWalk, Integer> {
    @Override
    Optional<FinishedWalk> findById(Integer id);

    List<FinishedWalk> findAllByUserId(Integer userId);
}
