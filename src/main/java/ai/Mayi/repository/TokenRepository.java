package ai.Mayi.repository;

import ai.Mayi.domain.Token;
import ai.Mayi.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TokenRepository extends JpaRepository<Token, Long> {
    List<Token> findByUser(User user);
}
