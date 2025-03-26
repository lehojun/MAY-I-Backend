package ai.Mayi.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Response {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long response_id;

    @Column(length = 30)
    private String response_type;

    private LocalDateTime response_at;


    // 메시지 message (Text) -> 줄바뀜을 쉽게 가져오려고한건데 찾아봐야함.
    @Column(columnDefinition = "TEXT") // MySQL의 TEXT 타입으로 매핑
    private String message;

    @OneToOne
    @JoinColumn(name = "chat_id")
    private Chat chat;

    public Response() {
        this.response_at = LocalDateTime.now();
    }
}
