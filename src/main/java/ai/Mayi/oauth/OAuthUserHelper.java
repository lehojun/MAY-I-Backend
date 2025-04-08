package ai.Mayi.oauth;

import ai.Mayi.domain.User;
import ai.Mayi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class OAuthUserHelper {
    private final UserRepository userRepository;
    
    // social login util
    public User findOrCreateUser(Map<String, Object> attributes) {
        String email = (String) attributes.get("email");
        return userRepository.findByUserEmail(email)
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .userEmail(email)
                            .userName((String) attributes.get("name"))
                            .social(true)
                            .build();
                    return userRepository.save(newUser);
                });
    }
}
