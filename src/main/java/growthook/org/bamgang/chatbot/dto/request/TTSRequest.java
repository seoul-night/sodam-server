package growthook.org.bamgang.chatbot.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class TTSRequest {
    private String model;
    private String input;
    private String voice;
}
