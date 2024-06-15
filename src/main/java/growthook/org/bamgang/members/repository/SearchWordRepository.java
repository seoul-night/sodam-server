package growthook.org.bamgang.members.repository;

import growthook.org.bamgang.members.domain.SearchWord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SearchWordRepository  extends JpaRepository<SearchWord, Integer> {
    List<SearchWord> getSearchWordsByUserId
}
