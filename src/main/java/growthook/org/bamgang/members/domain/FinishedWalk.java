package growthook.org.bamgang.members.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.bind.DefaultValue;

@Entity
@Getter
@Setter
@Table(name = "finishedtrail")
public class FinishedWalk {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer finishedId;

    private Integer trailId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer userId;

    private String review;

    @Column(insertable = false)
    private String walkedDate;
}
