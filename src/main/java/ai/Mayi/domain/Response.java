package ai.Mayi.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Response {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "response_id")
    private long responseId;

    @Column(name = "response_type", length = 30)
    private String responseType;

    @Column(name = "response_at")
    private LocalDateTime responseAt;

    @Column(columnDefinition = "TEXT") // MySQL의 TEXT 타입으로 매핑
    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id")
    private Chat chat;

    public Response() {
        this.responseAt = LocalDateTime.now();
    }
}
