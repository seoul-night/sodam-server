package growthook.org.bamgang.members.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetRegistLocationsResponseDto {
    private int id;
    private double latitude;
    private double longitude;
    private String name;
    private String address;
}
