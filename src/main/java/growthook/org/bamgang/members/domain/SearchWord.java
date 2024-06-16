package growthook.org.bamgang.members.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "searchword")
public class SearchWord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "word")
    private String word;

    @Column(name = "user_id")
    private Integer userId;

    @Column(insertable = false, name = "search_time")
    private LocalDateTime searchTime;
}
