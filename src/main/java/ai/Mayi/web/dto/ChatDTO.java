package ai.Mayi.web.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

public class ChatDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class ChatRequestDTO {
        @NotNull
        private String chatName;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatResponseDTO {
        private Long chatId;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatListResponseDTO {
        private Long chatId;
        private String chatName;
    }
}
