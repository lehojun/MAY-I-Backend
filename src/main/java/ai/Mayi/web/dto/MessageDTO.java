package ai.Mayi.web.dto;

import ai.Mayi.domain.enums.MessageType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

public class MessageDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "MESSAGE_REQ_01 : 채팅 입력 요청 DTO")
    public static class enterChatReqDTO {
        @NotNull
        private Long chatId;
        @NotNull
        private Long userId;
        @NotNull
        private List<MessageType> aiTypeList;
        @NotNull
        private String text;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "MESSAGE_DTO_01 : 채팅 응답 DTO")
    public static class ChatResDTO {
        @NotNull
        private MessageType messageType;
        @NotNull
        private String text;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "MESSAGE_REQ_01 : 채팅 입력 응답 DTO")
    public static class enterChatResDTO {
        @NotNull
        private Long chatId;
        @NotNull
        private List<ChatResDTO> responseDTOList;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class gptChatMessage {
        private String role;
        private String content;
    }

    @Data
    public static class gptReqDTO {

        private String model;
        private List<gptChatMessage> messages;
        private int n;

        public gptReqDTO(String model, String message) {
            this.model = model;
            this.messages = new ArrayList<gptChatMessage>();
            this.messages.add(new gptChatMessage("user",message));

            this.n = 1;
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class gptResDTO {

        private List<Choice> choices;

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Choice {
            private int index;
            private gptChatMessage message;
        }
    }
}
