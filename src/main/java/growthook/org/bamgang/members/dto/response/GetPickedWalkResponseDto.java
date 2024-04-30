package growthook.org.bamgang.members.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GetPickedWalkResponseDto {
    private Integer trailId;

    private String trailTitle;

    private String trailRegion;
}
