package growthook.org.bamgang.trail.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "safety2")
@Getter
public class Safety {
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name="type")
    private String type;

    @Column(name="latitude")
    private String latitude;

    @Column(name="longitude")
    private String longitude;
}
