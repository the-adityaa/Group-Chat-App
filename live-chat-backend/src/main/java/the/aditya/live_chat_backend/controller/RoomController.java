package the.aditya.live_chat_backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import the.aditya.live_chat_backend.entities.Message;
import the.aditya.live_chat_backend.entities.Room;
import the.aditya.live_chat_backend.repository.RoomRepository;
import the.aditya.live_chat_backend.util.RoomIdGenerator;

import java.util.Map;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/rooms")
public class RoomController {

    private final RoomRepository roomRepository;

    public RoomController(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    // Create Room

    @PostMapping
    public ResponseEntity<?> createRoom(@RequestBody Map<String, String> body) {

        String roomId = body.get("roomId");

        System.out.println("Create Room = [" + roomId + "]");

        if (roomRepository.findByRoomId(roomId).isPresent()) {
            return ResponseEntity.badRequest()
                    .body("Room with id : " + roomId + " already exists");
        }

        Room room = new Room();

        String id;
        do {
            id = RoomIdGenerator.generateRoomId();
        } while (roomRepository.existsById(id));

        room.setId(id);
        room.setRoomId(roomId);

        Room savedRoom = roomRepository.save(room);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedRoom);
    }

    // Join Room
    @GetMapping("/{roomId}")
    public ResponseEntity<?> joinRoom(@PathVariable String roomId) {

        System.out.println("Join Room = [" + roomId + "]");

        Optional<Room> room = roomRepository.findByRoomId(roomId);

        if (room.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("Room with id " + roomId + " does not exist");
        }

        return ResponseEntity.ok(room.get());
    }

    // Get Messages
    @GetMapping("/{roomId}/messages")
    public ResponseEntity<List<Message>> getMessages(
            @PathVariable String roomId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size
    ) {

        Optional<Room> room = roomRepository.findByRoomId(roomId);

        if (room.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        List<Message> messages = room.get().getMessages();

        int start = Math.max(0, messages.size() - (page + 1) * size);
        int end = Math.min(start + size, messages.size());

        return ResponseEntity.ok(messages.subList(start, end));
    }


}