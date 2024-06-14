package growthook.org.bamgang.trail.dto.response;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class GetNewTrailResponseDto {
    private Integer id;
    private String title;
    private String detail;
    private String image;
    private Double distance;
    private Double rating;
    private Double time;
    private String level;
    private String region;
    private Long cctvCount;
    private Long lightCount;
    private int safetyPercent;
    private boolean picked;
    private Double[] latitudeList;
    private Double[] longitudeList;
    private Double[] safetyLatitudeList;
    private Double[] safetyLongitudeList;
    private Integer[] safetyTypeList;
}

