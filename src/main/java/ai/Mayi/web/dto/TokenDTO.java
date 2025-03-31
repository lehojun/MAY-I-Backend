package ai.Mayi.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

public class TokenDTO {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "TOKEN_REQ_01 : 토큰 저장 요청 DTO_1")
    public static class tokenDto{
        @NotBlank
        @Schema(description = "토큰 타입", example = "gpt")
        String tokenType;
        @NotNull
        @Schema(description = "토큰 값", example = "qwerasdf1234")
        String value;
    }
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "TOKEN_REQ_01 : 토큰 저장 요청 DTO")
    public static class saveTokenReqDto{
        @NotNull
        @Schema(description = "유저 아이디", example = "1")
        Long userId;
        @NotBlank
        List<tokenDto> tokenList;
    }
}
