package ai.Mayi.service;

import ai.Mayi.repository.UserRepository;
import ai.Mayi.web.dto.UserDTO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        var result = userRepository.findByUserEmail(userEmail);
        if (result.isEmpty()) {
            throw new UsernameNotFoundException("say no email");
        }
        var user = result.get();
        List<GrantedAuthority> authorities = new ArrayList<>();
        if ("admin@auth.com".equals(userEmail)) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }
        var a = new CustomUser(user.getUserEmail(), user.getUserPassword(), authorities);
        a.setUserName(user.getUserName());
        a.setUserEmail(user.getUserEmail());
        return a;
    }

}

@Getter
@Setter
class CustomUser extends User{
    private String userName;
    private String userEmail;
    public CustomUser(String userEmail,
                      String userPassword,
                      Collection<? extends GrantedAuthority> authorities
    ) {
        super(userEmail, userPassword, authorities);
    }
}