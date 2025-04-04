package ai.Mayi.web.controller;
import ai.Mayi.service.UserServiceImpl;
import ai.Mayi.web.dto.JwtTokenDTO;
import ai.Mayi.web.dto.UserDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    public UserDTO.LoginResponseDTO login(@RequestBody UserDTO.LoginRequestDTO loginRequestDTO, HttpServletRequest request, HttpServletResponse response) throws Exception {
       return userserviceImpl.commonLogin(loginRequestDTO,request ,response);
    }

    @PostMapping("/logout")
    public void logout(HttpServletResponse response) {
        userserviceImpl.loginOut(response);
    }


    @PostMapping("/register")
    public String user_register(@RequestBody UserDTO.JoinRequestDTO joinDto) throws Exception {

        log.info("Registering post user: {}", joinDto.toString());

        userserviceImpl.signUp(joinDto);

        return "good";
    }

    @PostMapping("/test")
    public String test() {
        return "success";
    }

}
