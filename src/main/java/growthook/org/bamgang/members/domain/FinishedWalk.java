package growthook.org.bamgang.members.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "finishedtrail")
public class FinishedWalk {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Id
    private Integer finishedId;

    private Integer trailId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer userId;

    private String review;

    private String walkedDate;
}
