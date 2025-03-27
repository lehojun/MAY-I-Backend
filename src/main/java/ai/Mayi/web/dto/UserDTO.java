package ai.Mayi.web.dto;

import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

public class UserDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class UserRequestDTO {
        private Long user_id;
        @NotNull
        private String user_email;
        @NotNull
        private String user_name;
        @NotNull
        private String user_password;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserResponseDTO {
        private Long user_id;
        private String user_email;
    }
}
