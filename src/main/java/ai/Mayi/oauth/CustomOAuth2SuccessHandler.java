package ai.Mayi.oauth;

import ai.Mayi.jwt.CookieUtil;
import ai.Mayi.jwt.JwtUtil;
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

@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;
    private final OAuth2Properties oAuth2Properties;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        // 이메일 추출
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String userEmail = oAuth2User.getAttribute("email");


        // JWT 발급
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userEmail, null, authentication.getAuthorities());
        JwtTokenDTO jwtToken = jwtUtil.generateToken(token);

        // 쿠키에 저장
        cookieUtil.addCookie(response, "accessToken", jwtToken.getAccessToken(), 600);
        cookieUtil.addCookie(response, "refreshToken", jwtToken.getRefreshToken(), 3600);

        // 리다이렉트
        response.sendRedirect(oAuth2Properties.getSuccessRedirect());
    }
}

