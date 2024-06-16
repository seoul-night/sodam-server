package growthook.org.bamgang.members.repository;

import growthook.org.bamgang.members.domain.SearchWord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SearchWordRepository  extends JpaRepository<SearchWord, Integer> {
    List<SearchWord> getSearchWordsByUserIdOrderBySearchTimeDesc(int userId);

    void deleteAllByUserId(int userId);

    void deleteById(int id);

    Optional<SearchWord> findByUserIdAndWord(int userId, String word);
}
