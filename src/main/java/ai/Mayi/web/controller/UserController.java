package ai.Mayi.web.controller;
//import ai.Mayi.service.JwtUtil;
//import ai.Mayi.service.UserServiceImpl;
import ai.Mayi.service.JwtUtil;
import ai.Mayi.service.UserServiceImpl;
import ai.Mayi.web.dto.UserDTO;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
public class UserController {
    private final UserServiceImpl userserviceImpl;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @GetMapping("/login")
    public String login() {
        return "login.html";
    }

    @ResponseBody
    @PostMapping("/login")
    public UserDTO.LoginResponseDTO login(@RequestBody UserDTO.LoginRequestDTO loginRequestDTO) throws Exception {
       return userserviceImpl.commonLogin(loginRequestDTO);
    }


    @GetMapping("/register")
    public String register() {
        log.info("Registering get request");
        return "register.html";
    }

    @ResponseBody
    @PostMapping("/register")
    public String user_register(@RequestBody UserDTO.JoinRequestDTO joinDto) throws Exception {
        log.info("Registering post user: {}", joinDto.toString());
        userserviceImpl.signUp(joinDto);
        return "good";
    }


//    @PostMapping("/login/jwt")
//    @ResponseBody
//    public String loginJWT(@RequestBody Map<String, String> data, HttpServletResponse response){
//
//
//        log.info("Login request {}", data);
//
//        var authToken = new UsernamePasswordAuthenticationToken(
//                data.get("userEmail"), data.get("userPassword")
//        );
//
//        log.info("Login JWT: {}", authToken.toString());
//
//        var auth = authenticationManagerBuilder.getObject().authenticate(authToken);
//        SecurityContextHolder.getContext().setAuthentication(auth);
//        var auth2 = SecurityContextHolder.getContext().getAuthentication();
//        var jwt = JwtUtil.createToken(auth2);
//        log.info(jwt);
//
//        Cookie cookie = new Cookie("jwt", jwt);
//        cookie.setMaxAge(10);
//        cookie.setHttpOnly(true);
//        cookie.setPath("/");
//        response.addCookie(cookie);
//
//        return jwt;
//    }
//
//    @GetMapping("/my-page/jwt")
//    @ResponseBody
//    public String mypageJWT(HttpServletRequest request){
//        Cookie[] cookies = request.getCookies();
//        log.info("jwt cookies Name: {}", cookies[0].getName());
//        log.info("jwt cookies value: {}", cookies[0].getValue());
//
//        return "마이페이지데이터";
//    }
}
