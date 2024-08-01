package growthook.org.bamgang.members.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GetMemberResponseDto {
    private Integer userId;

    private String nickName;

    private Integer exp;

    private Integer walkedDay;

    private Integer finishedCount;

    private Integer pickedCount;

    private String profile;

    private String email;

    private Integer[] friendList;
}
