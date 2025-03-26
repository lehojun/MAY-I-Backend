package ai.Mayi.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chat_id;

    @Column(length = 100)
    private String chat_name;
    
    private LocalDateTime create_at;

    public Chat() {
        this.create_at = LocalDateTime.now();
    }

    //외래키로 User 엔티티의 user_id를 가져온다.
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
