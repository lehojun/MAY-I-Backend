package ai.Mayi.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatId;

    @Column(length = 100)
    private String chatName;
    
    private LocalDateTime createdAt;

    public Chat() {
        this.createdAt = LocalDateTime.now();
    }

    //외래키로 User 엔티티의 user_id를 가져온다.
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
}