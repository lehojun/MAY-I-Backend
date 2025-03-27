package ai.Mayi.web.controller;
import ai.Mayi.repository.UserRepository;
import ai.Mayi.service.UserService;
import ai.Mayi.web.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login.html";
    }

    @GetMapping("/register")
    public String register() {
        log.info("Registering get request");
        return "register.html";
    }

    @ResponseBody
    @PostMapping("/register")
    public String user_register(@RequestBody UserDTO.UserRequestDTO userDto) {
        log.info("Registering post user: {}", userDto.toString());
        userService.signUp(userDto);
        return "good";
    }
}
