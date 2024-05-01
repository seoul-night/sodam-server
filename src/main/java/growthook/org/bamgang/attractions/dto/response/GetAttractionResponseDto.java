package growthook.org.bamgang.attractions.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GetAttractionResponseDto {
    private String attractionRegion;

    private String attractionName;

    private String attractionLatitude;

    private String attractionLongitude;

    private String attractionDetail;

    private String attractionUrl;
}
