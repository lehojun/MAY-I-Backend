package ai.Mayi.oauth;

import ai.Mayi.apiPayload.code.status.ErrorStatus;
import ai.Mayi.apiPayload.exception.handler.SocialLoginHandler;
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

        String principalAttributionName = getPrincipalAttributeName(registrationId);
        log.info("attributes: {}", attributes);

        //email, profile picture
        String userEmail = userInfo.getEmail();
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
                principalAttributionName // Principal name
        );
    }

    private OAuth2UserInfo getUserInfo(String registrationId, Map<String, Object> attributes) {
        if ("google".equals(registrationId)) {
            return new GoogleUserInfo(attributes);
        } else if ("kakao".equals(registrationId)) {
            return new KakaoUserInfo(attributes);
        } else if ("naver".equals(registrationId)) {
            return new NaverUserInfo(attributes);
        } else {
            throw new SocialLoginHandler(ErrorStatus._INVALID_SOCIAL_LOGIN);
        }
    }

    private String getPrincipalAttributeName(String registrationId) {
        return switch (registrationId) {
            case "google" -> "email";
            case "kakao" -> "id";
            case "naver" -> "response";
            default -> throw new SocialLoginHandler(ErrorStatus._INVALID_SOCIAL_LOGIN);
        };
    }
}
