package ai.Mayi.service;

import ai.Mayi.domain.User;
import ai.Mayi.repository.UserRepository;
import ai.Mayi.web.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;

    public void signUp(UserDTO.JoinRequestDTO joinDto) throws Exception {

        var result = userRepository.findByUserEmail(joinDto.getUserEmail());
        if (result.isPresent()) {
            throw new Exception("존재하는 이메일");
        }

        String email = joinDto.getUserEmail();
        String username = joinDto.getUserName();
        String password = joinDto.getUserPassword();
        var hash_password = new BCryptPasswordEncoder().encode(password);

        User user = User.builder()
                .userEmail(email)
                .userName(username)
                .userPassword(hash_password).build();

        // 로그 찍어보기 나중에 지워야함
        log.info("Sign up email: " + user.getUserEmail());
        log.info("Sign up password: " + user.getUserPassword());
        log.info("Sign up user: " + user.getUserName());

        userRepository.save(user);
    }
}
