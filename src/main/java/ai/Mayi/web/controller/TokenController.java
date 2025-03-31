package ai.Mayi.web.controller;

import ai.Mayi.service.TokenService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "FolderController", description = "토큰 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/token")
public class TokenController {
    private final TokenService tokenService;

}
