package growthook.org.bamgang.members.dto.request;

import jakarta.persistence.Column;
import lombok.Getter;

@Getter
public class FinishedWalkRequest {
    private Integer finishedId;

    private Integer trailId;

    private String trailTitle;

    private Integer userId;

    private String review;

    private String walkedDate;
}
