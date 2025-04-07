package ai.Mayi.web.dto;

import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.List;

public class UserDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class JoinRequestDTO {
        @NotNull
        private String userEmail;
        @NotNull
        private String userName;
        @NotNull
        private String userPassword;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JoinResponseDTO {
        private Long userId;
        private String userEmail;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginRequestDTO {
        @NotNull
        private String userEmail;

        @NotNull
        private String userPassword;
    }

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserDataResponseDTO {
        private String userEmail;
        private String userName;
        private List<TokenDTO.tokenDto> tokenList;
    }
}
