package growthook.org.bamgang.members.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "pickedtrail")
public class PickedWalk {
    @Id
    private Integer trailId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer userId;

    private String trailTitle;

    private String trailRegion;


}
