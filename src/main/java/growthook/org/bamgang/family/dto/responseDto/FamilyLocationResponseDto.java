package growthook.org.bamgang.family.dto.responseDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FamilyLocationResponseDto {
    private Double latitude;
    private Double longitude;
}
