package growthook.org.bamgang.members.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "pickedtrail")
public class PickedWalk {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer trailId;

    private Integer userId;

    private String trailTitle;

    private String trailRegion;


}
