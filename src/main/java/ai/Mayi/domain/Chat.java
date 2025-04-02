package ai.Mayi.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "chat_name", length = 100)
    private String chatName;

    @Column(name = "create_at")
    private LocalDateTime createdAt;

    public Chat() {
        this.createdAt = LocalDateTime.now();
    }

    //외래키로 User 엔티티의 user_id를 가져온다.
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL)
    private List<Message> responses;
}