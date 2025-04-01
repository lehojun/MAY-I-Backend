package ai.Mayi.repository;

import ai.Mayi.domain.Chat;
import ai.Mayi.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findByUser(User user);
    Optional<Chat> findByChatId(Long chatId);
}
