package growthook.org.bamgang.trail.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class GetSearchTrailResponseDto {
    private double distance;
    private double time;
    private long cctvCount;
    private long lightCount;
    private int safetyPercent;
    private Double[] latitudeList;
    private Double[] longitudeList;
    private Double[] safetyLatitudeList;
    private Double[] safetyLongitudeList;
    private Integer[] safetyTypeList;
}
