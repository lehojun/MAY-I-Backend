package ai.Mayi.web.dto;

import ai.Mayi.domain.enums.MessageType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "BARD_REQ_01 : Bard 입력 요청 DTO")
    public static class BardContents {
        private String role;
        private List<Parts> parts;
        @Data
        @Builder
        public static class Parts {
            private String text;
        }
    }
    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "BARD_REQ_01 : Bard 입력 요청 DTO")
    public static class BardChatReqDTO {
        @NotNull
        private List<BardContents> contents;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "BARD_REQ_01 : Bard 입력 응답 DTO")
    public static class BardChatResDTO {
        @NotNull
        private List<Candidates> candidates;
        @NotNull
        private String modelVersion;
        @NotNull
        private Object usageMetadata;
        @Data
        public static class Candidates {
            private BardContents content;
            private String finishReason;
            private Double avgLogprobs;
        }

    }
}