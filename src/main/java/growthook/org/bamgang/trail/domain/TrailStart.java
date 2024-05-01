package growthook.org.bamgang.trail.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "trailstart")
@Getter
public class TrailStart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trail_id")
    private Integer trailId;

    @Column(name = "start_latitude1")
    private Double startLatitude1;

    @Column(name = "start_longitude1")
    private Double startLongitude1;

    @Column(name = "start_latitude2")
    private Double startLatitude2;

    @Column(name = "start_longitude2")
    private Double startLongitude2;

    @Column(name = "start_latitude3")
    private Double startLatitude3;

    @Column(name = "start_longitude3")
    private Double startLongitude3;
}
