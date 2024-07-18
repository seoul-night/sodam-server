package growthook.org.bamgang.members.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    private String nickName;

    private String password;

    private Integer exp;

    private Integer walkedDay;

    private Integer finishedCount;

    private Integer pickedCount;

    private String profile;

    private Integer familyId;

    private String email;
}
