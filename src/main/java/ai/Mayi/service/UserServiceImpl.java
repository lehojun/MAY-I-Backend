package ai.Mayi.service;

import ai.Mayi.domain.User;
import ai.Mayi.repository.UserRepository;
import ai.Mayi.web.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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

    public UserDTO.LoginResponseDTO commonLogin(UserDTO.LoginRequestDTO loginRequestDTO) throws Exception {
        String userEmail = loginRequestDTO.getUserEmail();
        String userPassword = loginRequestDTO.getUserPassword();

        Optional<User> user = userRepository.findByUserEmail(userEmail);

        if(user == null){
            log.error("존재하지 않는 이메일 입니다.");
            throw new Exception("존재하지 않는 이메일입니다.");
        }

        if(!passwordEncoder.matches(userPassword, user.get().getUserPassword())){
            log.error("비밀번호가 일치하지 않습니다.");
            log.error("입력된 비밀번호: {}", userPassword); // 로깅 추가
            log.error("저장된 해시 비밀번호: {}", user.get().getUserPassword()); // 로깅 추가
            throw new Exception("비밀번호가 일치하지 않습니다.");
        }

        return UserDTO.LoginResponseDTO.builder()
                .userId(user.get().getUserId())
                .build();
    }
}
