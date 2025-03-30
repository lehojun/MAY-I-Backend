package ai.Mayi.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Response {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long responseId;

    @Column(length = 30)
    private String responseType;

    private LocalDateTime responseAt;

    @Column(columnDefinition = "TEXT") // MySQL의 TEXT 타입으로 매핑
    private String message;

    @OneToOne
    @JoinColumn(name = "chatId")
    private Chat chat;

    public Response() {
        this.responseAt = LocalDateTime.now();
    }
}
