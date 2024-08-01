package growthook.org.bamgang.chatbot.controller;

import growthook.org.bamgang.chatbot.dto.request.MessageRequest;
import growthook.org.bamgang.chatbot.dto.request.SessionRequest;
import growthook.org.bamgang.chatbot.dto.response.MessageResponse;
import growthook.org.bamgang.chatbot.service.ChatbotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
