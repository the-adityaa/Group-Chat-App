package the.aditya.live_chat_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import the.aditya.live_chat_backend.entities.Room;

import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, String> {
    Optional<Room> findByRoomId(String roomId);

    Room save(Room room);
}
