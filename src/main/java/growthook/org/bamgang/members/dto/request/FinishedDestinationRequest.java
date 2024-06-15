package growthook.org.bamgang.members.dto.request;

import lombok.Getter;

@Getter
public class FinishedDestinationRequest {
    private Integer destinationId;
    private String destinationTitle;
    private Integer userId;
    private String review;
}
