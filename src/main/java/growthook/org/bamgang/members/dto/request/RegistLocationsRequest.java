package growthook.org.bamgang.members.dto.request;

import lombok.Data;

@Data
public class RegistLocationsRequest {
    private int userId;
    private double latitude;
    private double longitude;
    private String name;
    private String address;
}
