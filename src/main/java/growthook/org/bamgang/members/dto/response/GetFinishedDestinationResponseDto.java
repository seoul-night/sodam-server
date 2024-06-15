package growthook.org.bamgang.members.dto.response;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class GetFinishedDestinationResponseDto {
    private Integer finishedId;

    private Integer destinationId;

    private String destinationTitle;

    private String review;

    private LocalDate finishedDate;
}
