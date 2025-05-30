package ai.Mayi.web.controller;
import ai.Mayi.apiPayload.ApiResponse;
import ai.Mayi.jwt.CookieUtil;
import ai.Mayi.service.UserServiceImpl;
import ai.Mayi.web.dto.CommonDTO;
import ai.Mayi.web.dto.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserServiceImpl userserviceImpl;
    
    @PostMapping("/login")
    @Operation(summary = "로그인 API")
    public ApiResponse<String> login(@RequestBody UserDTO.LoginRequestDTO loginRequestDTO, HttpServletResponse response) {
       userserviceImpl.commonLogin(loginRequestDTO, response);
       return ApiResponse.onSuccess("로그인 완료되었습니다.");
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃 API")
    public ApiResponse<String> logout(HttpServletResponse response) {
        userserviceImpl.loginOut(response);
        return ApiResponse.onSuccess("로그아웃 완료되었습니다.");
    }


    @PostMapping("/register")
    @Operation(summary = "회원가입 API")
    public ApiResponse<CommonDTO.IsSuccessDTO> userRegister(@RequestBody UserDTO.JoinRequestDTO joinDto) {

        log.info("Registering post user: {}", joinDto.toString());

        return ApiResponse.onSuccess(userserviceImpl.signUp(joinDto));
    }

    @GetMapping("/data")
    @Operation(summary = "유저정보 조회 API")
    public ApiResponse<UserDTO.UserDataResponseDTO> userDataList (HttpServletRequest request) {
        String accessToken = CookieUtil.getCookieValue(request, "accessToken");
        return ApiResponse.onSuccess(userserviceImpl.sendUserData(accessToken));
    }
}
