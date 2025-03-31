package ai.Mayi.domain;

import ai.Mayi.domain.enums.ResponseType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Response {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "response_id")
    private long responseId;

    @Enumerated(EnumType.STRING)
    @Column(name = "response_type", length = 30)
    @NotNull
    private ResponseType responseType;

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
