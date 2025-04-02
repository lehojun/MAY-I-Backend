package ai.Mayi.service;

import ai.Mayi.apiPayload.code.status.ErrorStatus;
import ai.Mayi.apiPayload.exception.handler.MessageHandler;
import ai.Mayi.domain.User;
import ai.Mayi.jwt.CookieUtil;
import ai.Mayi.jwt.JwtUtil;
import ai.Mayi.repository.UserRepository;
import ai.Mayi.web.dto.JwtTokenDTO;
import ai.Mayi.web.dto.UserDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Response;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtUtil jwtUtil;

    public void signUp(UserDTO.JoinRequestDTO joinDto) throws Exception {

        var result = userRepository.findByUserEmail(joinDto.getUserEmail());
        if (result.isPresent()) {
            throw new Exception("존재하는 이메일");
        }

        String email = joinDto.getUserEmail();
        String username = joinDto.getUserName();
        String password = joinDto.getUserPassword();
        String role = "USER";
        var hash_password = new BCryptPasswordEncoder().encode(password);

        User user = User.builder()
                .userEmail(email)
                .userName(username)
                .userPassword(hash_password)
                .roles(List.of(role))
                .build();

        // 로그 찍어보기 나중에 지워야함
        log.info("Sign up email: " + user.getUserEmail());
        log.info("Sign up password: " + user.getUserPassword());
        log.info("Sign up user: " + user.getUsername());

        userRepository.save(user);
    }

    public User findUserById(Long userId){
        return userRepository.findByUserId(userId).orElseThrow(() -> new MessageHandler(ErrorStatus._NOT_EXIST_USER));
    }

    @Transactional
    public JwtTokenDTO commonLogin(UserDTO.LoginRequestDTO loginRequestDTO, HttpServletRequest request, HttpServletResponse response) throws Exception {

        String userEmail = loginRequestDTO.getUserEmail();
        String userPassword = loginRequestDTO.getUserPassword();

        Optional<User> user = userRepository.findByUserEmail(userEmail);
        
        //처음 로그인 할때
//        if(CookieUtil.getCookieValue(request, ))
        if (user == null) {
            log.error("존재하지 않는 이메일 입니다.");
            throw new Exception("존재하지 않는 이메일입니다.");
        }

        if (!passwordEncoder.matches(userPassword, user.get().getUserPassword())) {
            log.error("비밀번호가 일치하지 않습니다.");
            log.error("입력된 비밀번호: {}", userPassword); // 로깅 추가
            log.error("저장된 해시 비밀번호: {}", user.get().getUserPassword()); // 로깅 추가
            throw new Exception("비밀번호가 일치하지 않습니다.");
        }

        String existingRefreshToken = user.get().getRefreshToken();

        if(existingRefreshToken == null) {
            // new JWT token create

        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userEmail, userPassword);

        Authentication authentication = authenticationManagerBuilder
                .getObject().authenticate(authenticationToken);

        JwtTokenDTO jwtTokenDTO = jwtUtil.generateToken(authentication);

        log.info("access token: {}", jwtTokenDTO.getAccessToken());
        log.info("refresh token: {}", jwtTokenDTO.getRefreshToken());


        //아직 처음 로그인했을때 그런거 설정안함
        var refreshToken = jwtTokenDTO.getRefreshToken();
        user.get().updateRefreshToken(refreshToken);
        userRepository.save(user.get());

        //쿠키에 AccessToken, RefreshToken save
        sendTokenResponse(response, jwtTokenDTO);


        UserDTO.LoginResponseDTO.builder()
                .userId(user.get().getUserId())
                .build();

        return jwtTokenDTO;
    }

    public void sendTokenResponse(HttpServletResponse response, JwtTokenDTO jwtTokenDTO){
        // Cookie AccessToken lifeTime : 1m
        CookieUtil.addCookie(response, "accessToken", jwtTokenDTO.getAccessToken(), 600);
        // Cookie RefreshToken lifeTime : 1h
        CookieUtil.addCookie(response, "refreshToken", jwtTokenDTO.getRefreshToken(), 3600);
    }
}
