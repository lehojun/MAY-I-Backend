package ai.Mayi.service;

import javax.crypto.SecretKey;

import ai.Mayi.web.dto.JwtToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtUtil {
    private static SecretKey key;

    public JwtUtil(@Value("${jwt.secret}") String jwtkey) {
        byte[] keyBytes = Decoders.BASE64.decode(jwtkey);
        key = Keys.hmacShaKeyFor(keyBytes);
    }


    // JWT create
    public static JwtToken createToken(Authentication auth) {

        var user = (CustomUser) auth.getPrincipal();

        //List -> String
        String authorities = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));

        String accessToken = Jwts.builder()
                .claim("userEmail", user.getUserEmail())
                .claim("auth", authorities) //권한
                .issuedAt(new Date(System.currentTimeMillis())) //jwt 발행
                .expiration(new Date(System.currentTimeMillis() + 100000)) //ms 단위
                .signWith(key) //해싱
                .compact();

        String refreshToken = Jwts.builder()
                .expiration(new Date(System.currentTimeMillis() + 3600000)) //ms 단위
                .signWith(key) //해싱
                .compact();

        return JwtToken.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

//        >> return 타입 String으로 변환 [단순히 accessToken만 발행]
//        return Jwts.builder()
//                .claim("userEmail", user.getUserEmail())
//                .claim("auth", authorities) //권한
//                .issuedAt(new Date(System.currentTimeMillis())) //jwt 발행
//                .expiration(new Date(System.currentTimeMillis() + 100000)) //ms 단위
//                .signWith(key) //해싱
//                .compact();
    }

//    // JWT extract
//    public static Claims extractToken(String token) {
//
//        return Jwts.parser().verifyWith(key).build()
//                .parseSignedClaims(token).getPayload(); //유저정보
//    }

    //토큰 정보 체크 메서드


}