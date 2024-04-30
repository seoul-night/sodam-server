package growthook.org.bamgang.members.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GetFinishedWalkResponseDto {
    private Integer trailId;

    private String trailTitle;

    private String review;

    private String walkedDate;
}
