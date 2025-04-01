package ai.Mayi.repository;

import ai.Mayi.domain.Chat;
import ai.Mayi.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface ChatRepository extends JpaRepository<Chat, Integer> {
    List<Chat> findByUser(User user);
}
