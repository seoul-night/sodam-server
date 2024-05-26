package growthook.org.bamgang.members.dto.token;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class MemberToken {
    private String nickName;

    private String profile;

    private String id;
}
