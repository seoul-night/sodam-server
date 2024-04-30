package growthook.org.bamgang.trail.dto.response;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GetTrailResponseDto {
    private Integer id;
    private String title;
    private String detail;
    private String image;
    private Double distance;
    private Integer[] cctvArr;
    private Double rating;
    private Double time;
    private String level;
    private String region;
}
