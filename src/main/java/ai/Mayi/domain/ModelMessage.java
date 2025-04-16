package ai.Mayi.domain;

import ai.Mayi.domain.enums.MessageType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Entity
@Getter
@DynamicUpdate
@DynamicInsert
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ModelMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "model_message_id")
    private Long modelMessageId;

    @Enumerated(EnumType.STRING)
    @Column(name = "message_type", length = 30)
    @NotNull
    @Setter
    private MessageType messageType;

    @Column(name = "message_at")
    private LocalDateTime messageAt;

    @Column(columnDefinition = "TEXT") // MySQL의 TEXT 타입으로 매핑
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id")
    private Message message;
}
