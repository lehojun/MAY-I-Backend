package ai.Mayi.web.controller;

import ai.Mayi.apiPayload.ApiResponse;
import ai.Mayi.service.TokenService;
import ai.Mayi.web.dto.CommonDTO;
import ai.Mayi.web.dto.TokenDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "TokenController", description = "토큰 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("token")
public class TokenController {
    private final TokenService tokenService;

    @PostMapping("")
    @Operation(summary = "토큰 저장 API")
    public ApiResponse<CommonDTO.IsSuccessDTO> saveToken(@RequestBody @Valid TokenDTO.saveTokenReqDto request) {

        return ApiResponse.onSuccess(tokenService.saveToken(request));
    }

    @GetMapping("")
    @Operation(summary = "토큰 조회 API")
    public ApiResponse<TokenDTO.getTokenResDto> getToken(@RequestParam @Valid Long userId) {

        return ApiResponse.onSuccess(tokenService.getToken(userId));
    }
}
