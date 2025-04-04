package ai.Mayi.jwt;

import javax.crypto.SecretKey;

import ai.Mayi.apiPayload.code.status.ErrorStatus;
import ai.Mayi.apiPayload.exception.handler.JwtHandler;
import ai.Mayi.apiPayload.exception.handler.UserHandler;
import ai.Mayi.domain.User;
import ai.Mayi.repository.UserRepository;
import ai.Mayi.web.dto.JwtTokenDTO;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtUtil {
    private static SecretKey key;
    private static UserRepository userRepository;

    public JwtUtil(@Value("${jwt.secret}") String jwtkey) {
        byte[] keyBytes = Decoders.BASE64.decode(jwtkey);
        key = Keys.hmacShaKeyFor(keyBytes);
    }

    public static JwtTokenDTO generateToken(Authentication authentication) {
        String accessToken = generateAccessToken(authentication);
        String refreshToken = generateRefreshToken(authentication);

        return JwtTokenDTO.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public static String generateAccessToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = System.currentTimeMillis();
        Date accessTokenExpiresIn = new Date(now + 600000);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim("auth", authorities)
                .expiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public static String generateRefreshToken(Authentication authentication) {
        long now = System.currentTimeMillis();
        Date refreshTokenExpiresIn = new Date(now + 3600000);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .expiration(refreshTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    //jwt Token 복호화
    public Authentication getAuthentication(String accessToken) {

        Claims claims = parseClaims(accessToken);

        if (claims.get("auth") == null) {
            throw new RuntimeException("accessToken에 권한 정보가 없습니다.");
        }

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("auth").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        UserDetails principal = new org.springframework.security.core.userdetails.User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }


    public boolean validateToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            log.info("JWT claims string is empty.");
            return false;
        }

        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            log.info("Invalid JWT Token: {}", e.getMessage());
        }

        return false;
    }


    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(accessToken)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            log.error("parseClaims 메서드 예외 처리");
            throw new JwtHandler(ErrorStatus._INVALID_JWT);
        }
    }

    public String getUserEmail(String token) {
        return parseClaims(token).getSubject();
    }

    public Long getUserId(String userEmail) {
        Optional<ai.Mayi.domain.User> userOpt = userRepository.findByUserEmail(userEmail);
        User user = userOpt.get();
        return user.getUserId();
    }
}