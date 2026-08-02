package the.aditya.live_chat_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import the.aditya.live_chat_backend.entities.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
}
