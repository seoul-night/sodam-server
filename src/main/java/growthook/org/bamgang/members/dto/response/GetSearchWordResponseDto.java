package growthook.org.bamgang.members.dto.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GetSearchWordResponseDto {
    private int id;
    private String word;
}
