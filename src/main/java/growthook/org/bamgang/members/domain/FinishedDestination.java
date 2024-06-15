package growthook.org.bamgang.members.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "finisheddestionation")
public class FinishedDestination {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "finished_id")
    private Integer finishedId;

    @Column(name = "destination_id")
    private Integer destinationId;

    @Column(name = "destinaion_title")
    private String destinationTitle;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "review")
    private String review;

    @Column(insertable = false, name = "finished_date")
    private LocalDate finishedDate;
}
