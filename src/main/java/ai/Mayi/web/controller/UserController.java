package ai.Mayi.web.controller;
import ai.Mayi.service.UserServiceImpl;
import ai.Mayi.web.dto.JwtTokenDTO;
import ai.Mayi.web.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserServiceImpl userserviceImpl;

    @PostMapping("/login")
    public JwtTokenDTO login(@RequestBody UserDTO.LoginRequestDTO loginRequestDTO) throws Exception {

       return userserviceImpl.commonLogin(loginRequestDTO);

    }


    @PostMapping("/register")
    public String user_register(@RequestBody UserDTO.JoinRequestDTO joinDto) throws Exception {

        log.info("Registering post user: {}", joinDto.toString());

        userserviceImpl.signUp(joinDto);

        return "good";
    }

    @PostMapping("/sign-in")
    public JwtTokenDTO signIn(@RequestBody UserDTO.LoginRequestDTO loginRequestDTO) throws Exception {

        log.info("로그인 접속 시도 : {}", loginRequestDTO.toString());

        String username = loginRequestDTO.getUserEmail();
        String password = loginRequestDTO.getUserPassword();

        JwtTokenDTO jwtTokenDTO = userserviceImpl.commonLogin(loginRequestDTO);

        log.info("request username = {}, password = {}", username, password);
        log.info("jwtToken accessToken = {}, refreshToken = {}", jwtTokenDTO.getAccessToken(), jwtTokenDTO.getRefreshToken());

        return jwtTokenDTO;
    }

    @PostMapping("/test")
    public String test() {
        return "success";
    }
}
