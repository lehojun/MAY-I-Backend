package ai.Mayi.repository;

import ai.Mayi.domain.ModelMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModelMessageRepository extends JpaRepository<ModelMessage, Long> {
}
