package ai.Mayi.domain;

import ai.Mayi.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserSaveTest {

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void delete() {
        userRepository.deleteAll();
    }

    @Test
    void test() {
        //given
        User user = User.builder()
                .user_email("test@email.com")
                .user_name("test man01")
                .password("secretPassword01")
                .build();
        //when
        userRepository.save(user);

        //then
        User savedUser = userRepository.findById(Math.toIntExact(user.getUser_id())).orElseThrow();
        Assertions.assertThat(savedUser.getUser_email()).isEqualTo(user.getUser_email());
        Assertions.assertThat(savedUser.getUser_name()).isEqualTo(user.getUser_name());
        Assertions.assertThat(savedUser.getPassword()).isEqualTo(user.getPassword());
    }

}