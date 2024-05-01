package growthook.org.bamgang.trail.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class GetTrailResponseDto {
    private Integer id;
    private String title;
    private String detail;
    private String image;
    private Double distance;
    private Double rating;
    private Double time;
    private String level;
    private String region;
    private Double[] latitudeList;
    private Double[] longitudeList;

}
