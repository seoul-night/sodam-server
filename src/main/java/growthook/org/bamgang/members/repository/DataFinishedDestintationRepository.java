package growthook.org.bamgang.members.repository;

import growthook.org.bamgang.members.domain.FinishedDestination;
import growthook.org.bamgang.members.domain.FinishedWalk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DataFinishedDestintationRepository extends JpaRepository<FinishedDestination, Integer> {

}
