package growthook.org.bamgang.chatbot.controller;

import growthook.org.bamgang.chatbot.dto.request.MessageRequest;
import growthook.org.bamgang.chatbot.dto.request.SessionRequest;
import growthook.org.bamgang.chatbot.dto.request.TextRequest;
import growthook.org.bamgang.chatbot.dto.response.MessageResponse;
import growthook.org.bamgang.chatbot.service.ChatbotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;

@RestController
@RequestMapping("/chatbots")
public class ChatbotController {
    @Autowired
    private ChatbotService chatbotService;

    @PostMapping()
    public ResponseEntity<?> postMessage(@RequestBody MessageRequest request) {
        try {
            MessageResponse response = chatbotService.getMessage(request);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/speach")
    public ResponseEntity<?> textToSpeach(@RequestBody TextRequest request) {
        byte[] speach = chatbotService.textToSpeach(request.getText());

        // InputStreamResource 생성
        InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(speach));

        // 응답 헤더 설정
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        responseHeaders.setContentDispositionFormData("attachment", "speech.mp3");

        // ResponseEntity 반환
        return ResponseEntity.ok()
                .headers(responseHeaders)
                .contentLength(speach.length)
                .body(resource);
    }

    @PostMapping("/sessions")
    public ResponseEntity<?> postSession(@RequestBody SessionRequest session) {
        try{
            chatbotService.postSession(session);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
