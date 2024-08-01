package growthook.org.bamgang.chatbot.repository;

import growthook.org.bamgang.chatbot.entity.Chatbot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatbotRepository extends JpaRepository<Chatbot,Integer> {
}
