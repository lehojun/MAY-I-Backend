//package ai.Mayi.jwt;
//
//import ai.Mayi.service.JwtUtil;
//import io.jsonwebtoken.Claims;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//@Slf4j
//public class JwtFilter extends OncePerRequestFilter {
//
//    @Override
//    protected void doFilterInternal(
//            HttpServletRequest request,
//            HttpServletResponse response,
//            FilterChain filterChain
//    ) throws ServletException, IOException {
//
//        // get Cookie
//        Cookie[] cookies = request.getCookies();
//        if (cookies == null){
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//
//        var jwtCookie = "";
//        for (int i = 0; i < cookies.length; i++){
//            if (cookies[i].getName().equals("jwt")){
//                jwtCookie = cookies[i].getValue();
//            }
//        }
//        log.info("JWT cookie is {}", jwtCookie);
//
//        Claims claim;
//        try {
//            claim = JwtUtil.extractToken(jwtCookie);
//        }catch (Exception e){
//            filterChain.doFilter(request, response);
//            log.info("JWT token is empty");
//            return;
//        }
//
//        log.info("JWT token Name is {}", claim.get("userEmail").toString());
//
//        filterChain.doFilter(request, response);
//    }
//
//}
