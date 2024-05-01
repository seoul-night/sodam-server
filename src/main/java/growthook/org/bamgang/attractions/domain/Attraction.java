package growthook.org.bamgang.attractions.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "attraction")
public class Attraction {
    @Id
    private Integer attractionId;

    private String attractionRegion;

    private String attractionName;

    private String attractionLatitude;

    private String attractionLongitude;

    private String attractionDetail;

    private String attractionUrl;
}
