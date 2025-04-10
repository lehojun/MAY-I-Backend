package ai.Mayi.oauth;

import ai.Mayi.apiPayload.code.status.ErrorStatus;
import ai.Mayi.apiPayload.exception.handler.SocialLoginHandler;
import ai.Mayi.domain.User;
import ai.Mayi.jwt.CookieUtil;
import ai.Mayi.jwt.JwtUtil;
import ai.Mayi.repository.UserRepository;
import ai.Mayi.web.dto.JwtTokenDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;
    private final OAuth2Properties oAuth2Properties;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String registrationId = (String) request.getSession().getAttribute("registrationId");

        OAuth2UserInfo userInfo = getUserInfo(registrationId, oAuth2User.getAttributes());

        String userEmail = userInfo.getEmail();

        Optional<User> user = userRepository.findByUserEmail(userEmail);


        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userEmail, null, authentication.getAuthorities());
        JwtTokenDTO jwtToken = jwtUtil.generateToken(token);

        cookieUtil.addCookie(response, "accessToken", jwtToken.getAccessToken(), 600);
        cookieUtil.addCookie(response, "refreshToken", jwtToken.getRefreshToken(), 3600);


        user.ifPresent(u -> {
            u.updateRefreshToken(jwtToken.getRefreshToken());
            userRepository.save(u);
        });

        response.sendRedirect(oAuth2Properties.getSuccessRedirect());
    }

    private OAuth2UserInfo getUserInfo(String registrationId, Map<String, Object> attributes) {
        if ("google".equals(registrationId)) return new GoogleUserInfo(attributes);
        else if ("kakao".equals(registrationId)) return new KakaoUserInfo(attributes);
        else if ("naver".equals(registrationId)) return new NaverUserInfo(attributes);
        else throw new SocialLoginHandler(ErrorStatus._INVALID_SOCIAL_LOGIN);
    }
}

