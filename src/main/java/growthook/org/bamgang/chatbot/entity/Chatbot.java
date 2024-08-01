package growthook.org.bamgang.chatbot.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "chatbot")
public class Chatbot {

    @Id
    @Column(name = "member_id")
    private int memberId;

    @Column(name = "thread_id")
    private String threadId;
}
