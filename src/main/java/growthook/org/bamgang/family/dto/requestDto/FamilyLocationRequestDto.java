package growthook.org.bamgang.family.dto.requestDto;

import lombok.Data;

@Data
public class FamilyLocationRequestDto {
    private Integer userId;
    private Double latitude;
    private Double longitude;
}
