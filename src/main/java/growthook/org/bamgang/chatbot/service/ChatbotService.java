package growthook.org.bamgang.chatbot.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import growthook.org.bamgang.chatbot.dto.request.MessageRequest;
import growthook.org.bamgang.chatbot.dto.request.SessionRequest;
import growthook.org.bamgang.chatbot.dto.response.MessageResponse;
import growthook.org.bamgang.chatbot.entity.Chatbot;
import growthook.org.bamgang.chatbot.repository.ChatbotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatbotService {
    @Autowired
    ChatbotRepository chatbotRepository;

    @Value("${gpt-key}")
    private String gptKey;
    // assistent ID
    private String assistentId = "asst_QSo0OiudWg3t8pPv1XJuP9hb";

    private String createThreadId() {
        String URL = "https://api.openai.com/v1/threads";
        try {
            // HTTP 요청 설정
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            headers.set("Authorization", "Bearer " + gptKey);
            headers.set("OpenAI-Beta", "assistants=v2");
            HttpEntity<String> entity = new HttpEntity<>("", headers);

            // API 호출
            ResponseEntity<String> response = restTemplate.exchange(URL, HttpMethod.POST, entity, String.class);
            String jsonResponse = response.getBody();

            // JSON 응답에서 id 추출
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            return rootNode.path("id").asText();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getThreadId(int memberId) {
        try {
            Chatbot chatbot = chatbotRepository.findById(memberId).orElseThrow(Exception::new);
            return chatbot.getThreadId();
        } catch (Exception e){
            String thread = createThreadId();
            Chatbot chatbot = new Chatbot();
            chatbot.setThreadId(thread);
            chatbot.setMemberId(memberId);
            chatbotRepository.save(chatbot);
            return thread;
        }
    }

    private void sendMessage(String threadId, String messageContent) {
        String BASE_URL = "https://api.openai.com/v1/threads/";
        try {
            // HTTP 요청 설정
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            headers.set("Authorization", "Bearer " + gptKey);
            headers.set("OpenAI-Beta", "assistants=v2");

            // 요청 바디 설정
            String requestBody = String.format(
                    "{\"role\": \"user\", \"content\": \"%s\"}",
                    messageContent
            );
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

            // API 호출
            String url = BASE_URL + threadId + "/messages";
            restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    private void runMessages(String threadId, String assistantId) {
        String BASE_URL = "https://api.openai.com/v1/threads/";
        try {
            // HTTP 요청 설정
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + gptKey);
            headers.set("Content-Type", "application/json");
            headers.set("OpenAI-Beta", "assistants=v2");

            // 요청 바디 설정
            String requestBody = String.format(
                    "{\"assistant_id\": \"%s\"}",
                    assistantId
            );
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

            // API 호출
            String url = BASE_URL + threadId + "/runs";
            restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    public List<String> getMessageTextValues(String threadId) {
        String BASE_URL = "https://api.openai.com/v1/threads/";
        try {
            // HTTP 요청 설정
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            headers.set("Authorization", "Bearer " + gptKey);
            headers.set("OpenAI-Beta", "assistants=v2");

            HttpEntity<String> entity = new HttpEntity<>(null, headers);

            // API 호출
            String url = BASE_URL + threadId + "/messages";
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            // 응답 처리
            String jsonResponse = response.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            List<String> textValues = new ArrayList<>();

            // 메시지에서 text value 추출
            for (JsonNode messageNode : rootNode.path("data")) {
                for (JsonNode contentNode : messageNode.path("content")) {
                    if (contentNode.path("type").asText().equals("text")) {
                        String textValue = contentNode.path("text").path("value").asText();
                        textValues.add(textValue);
                    }
                }
            }

            return textValues;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public byte[] textToSpeach(String message) {
        String url = "https://api.openai.com/v1/audio/speech";

        // HTTP 요청을 위한 RestTemplate 인스턴스 생성
        RestTemplate restTemplate = new RestTemplate();

        // 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + gptKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 요청 바디 설정
        String requestBody = "{"
                + "\"model\": \"tts-1\","
                + "\"input\": \""+message+"\","
                + "\"voice\": \"echo\""
                + "}";

        // HTTP 엔티티 생성
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        // API 호출
        ResponseEntity<byte[]> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, byte[].class);

        // 받아온 MP3 데이터를 바이트 배열로 변환
        return response.getBody();
    }

    public MessageResponse getMessage(MessageRequest messageRequest) throws Exception{
        int memberId = messageRequest.getMemberId();
        String threadId = getThreadId(memberId);
        sendMessage(threadId,messageRequest.getChat());
        runMessages(threadId,assistentId);
        Thread.sleep(3000);
        List<String> messages = getMessageTextValues(threadId);
        MessageResponse response = MessageResponse.builder()
                .chat(messages.get(0))
                .build();
        return response;
    }

    public void postSession(SessionRequest request) throws Exception {
        int memberId = request.getMemberId();
        chatbotRepository.deleteById(memberId);
        String threadId = getThreadId(memberId);
        sendMessage(threadId,"지금 내가 있는 곳은 " + request.getChat() + " 이야. 알아줘.");
        runMessages(threadId,assistentId);
    }
}
