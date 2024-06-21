package growthook.org.bamgang.trail.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class GetPopularTrailResponseDto {
    private Integer trailId;
    private String title;
    private String detail;
    private String image;
    private Double distance;
    private Double rating;
    private Double time;
    private String level;
    private String region;
    private Double[] startLatitudeList;
    private Double[] startLongitudeList;
    private Double[] safetyLatitudeList;
    private Double[] safetyLongitudeList;
    private Double[] latitudeList;
    private Double[] longitudeList;
    private Integer[] safetyTypeList;
    private boolean picked;
}
