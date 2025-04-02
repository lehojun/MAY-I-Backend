package ai.Mayi.config;

//import ai.Mayi.jwt.JwtAuthenticationFilter;
import ai.Mayi.jwt.JwtAuthenticationFilter;
import ai.Mayi.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtUtil jwtUtil;
    
    //비밀번호 해싱하는 코드
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // REST API이므로 기본 인증 및 CSRF 보호 비활성화
                .httpBasic(httpBasic -> httpBasic.disable())
                .csrf(csrf -> csrf.disable())
                // JWT를 사용하기 때문에 세션을 사용하지 않음
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 요청에 대한 인증 및 권한 설정
                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/swagger-ui/index.html").permitAll() //
//                        .requestMatchers("/user/sign-in", "user/register").permitAll() // 로그인 API는 인증 없이 접근 가능
//                        .requestMatchers("/user/register").permitAll() // 로그인 API는 인증 없이 접근 가능
                        .requestMatchers("/**").permitAll() //
                        .requestMatchers("/user/test").hasRole("USER") // "USER" 역할 필요
                        .anyRequest().authenticated() // 나머지 요청은 인증 필요
                )
                // JWT 인증 필터를 UsernamePasswordAuthenticationFilter 전에 추가
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}