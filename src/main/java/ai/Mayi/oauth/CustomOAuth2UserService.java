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

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        //email, profile picture
        String userEmail = (String) attributes.get("email");
        String userPicture = (String) attributes.get("picture");

        // DB에 있는지 확인하고 없으면 새로 저장
        Optional<User> userOptional = userRepository.findByUserEmail(userEmail);
        User user = userOptional.orElseGet(() -> {
            log.info("현재 소셜로그인 후 DB에 유저 저장 진행 중..");
            User newUser = User.builder()
                    .userEmail(userEmail)
                    .userName((String) attributes.get("name"))
                    .social(true)
                    .profileImageUrl(userPicture)
                    // dummyPassword
                    .userPassword(UUID.randomUUID().toString())
                    .build();
            return userRepository.save(newUser);
        });

        return new CustomOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                attributes,
                "email" // Principal name
        );
    }
}
