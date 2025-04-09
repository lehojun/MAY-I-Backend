package ai.Mayi.oauth;

import ai.Mayi.domain.User;
import ai.Mayi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2UserInfo userInfo = getUserInfo(registrationId, attributes);

        //email, profile picture
        String userEmail = userInfo.getEmail();
        if (userEmail == null) {
            log.error("카카오에서 이메일 못 받아옴: {}", attributes);
            throw new RuntimeException("카카오 로그인 실패 - 이메일 없음");
        }
        String userName = userInfo.getName();
        String userPicture = userInfo.getImageUrl();
        String role = "USER";

        // DB에 있는지 확인하고 없으면 새로 저장
        Optional<User> userOptional = userRepository.findByUserEmail(userEmail);
        User user = userOptional.orElseGet(() -> {
            log.info("현재 소셜로그인 후 DB에 유저 저장 진행 중..");
            User newUser = User.builder()
                    .userEmail(userEmail)
                    .userName(userName)
                    // dummyPassword
                    .userPassword(UUID.randomUUID().toString())
                    .roles(List.of(role))
                    .social(true)
                    .profileImageUrl(userPicture)
                    .build();
            return userRepository.save(newUser);
        });
        return new CustomOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                attributes,
                "id" // Principal name
        );
    }
    private OAuth2UserInfo getUserInfo(String registrationId, Map<String, Object> attributes) {
        if ("google".equals(registrationId)) {
            return new GoogleUserInfo(attributes);
        } else if ("kakao".equals(registrationId)) {
            return new KakaoUserInfo(attributes);
        } else {
            throw new RuntimeException("Unsupported social login provider: " + registrationId);
        }
    }
}
