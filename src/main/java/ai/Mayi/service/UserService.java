package ai.Mayi.service;

import ai.Mayi.domain.User;
import ai.Mayi.repository.UserRepository;
import ai.Mayi.web.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void signUp(UserDTO.UserRequestDTO userDto) {
        String email = userDto.getUser_email();
        String username = userDto.getUser_name();
        String password = userDto.getUser_password();

        User user = User.builder()
                .user_email(email)
                .user_name(username)
                .user_password(password).build();

        log.info("Sign up email: " + user.getUser_email());
        log.info("Sign up password: " + user.getUser_password());
        log.info("Sign up user: " + user.getUser_name());

        userRepository.save(user);
    }
}
