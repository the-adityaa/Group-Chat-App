package the.aditya.live_chat_backend.controller;


import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import the.aditya.live_chat_backend.entities.Message;
import the.aditya.live_chat_backend.entities.Room;
import the.aditya.live_chat_backend.payload.MessageRequest;
import the.aditya.live_chat_backend.repository.MessageRepository;
import the.aditya.live_chat_backend.repository.RoomRepository;
import java.util.UUID;
import java.time.LocalDateTime;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import the.aditya.live_chat_backend.payload.OnlineUsersResponse;
import the.aditya.live_chat_backend.payload.UserPresence;
import the.aditya.live_chat_backend.service.OnlineUserService;


@Controller
public class ChatController {

    private final RoomRepository roomRepository;
    private final MessageRepository messageRepository;
    private final OnlineUserService onlineUserService;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(RoomRepository roomRepository,
                          MessageRepository messageRepository,
                          OnlineUserService onlineUserService,
                          SimpMessagingTemplate messagingTemplate) {

        this.roomRepository = roomRepository;
        this.messageRepository = messageRepository;
        this.onlineUserService = onlineUserService;
        this.messagingTemplate = messagingTemplate;
    }

    // for sending and receiving messages

    @MessageMapping("/join")
    public void joinRoom(@RequestBody UserPresence request) {

        onlineUserService.addUser(request.getRoomId(), request.getUsername());

        messagingTemplate.convertAndSend(
                "/topic/room/" + request.getRoomId() + "/users",
                new OnlineUsersResponse(
                        request.getRoomId(),
                        onlineUserService.getUsers(request.getRoomId())
                )
        );
    }

    @MessageMapping("/leave")
    public void leaveRoom(@RequestBody UserPresence request) {

        onlineUserService.removeUser(request.getRoomId(), request.getUsername());

        messagingTemplate.convertAndSend(
                "/topic/room/" + request.getRoomId() + "/users",
                new OnlineUsersResponse(
                        request.getRoomId(),
                        onlineUserService.getUsers(request.getRoomId())
                )
        );
    }

    @MessageMapping("/sendMessage/{roomId}")
    @SendTo("/topic/room/{roomId}")
    public Message sendMessage(
            @DestinationVariable String roomId,
            @RequestBody MessageRequest request
    ) {
        Room room = roomRepository.findByRoomId(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

            Message message = new Message();
            message.setId(UUID.randomUUID().toString());

            message.setContent(request.getContent());
            message.setSender(request.getSender());
            message.setTimeStamp(LocalDateTime.now());

            message.setMessageType(request.getMessageType());
            message.setFileName(request.getFileName());
            message.setFileUrl(request.getFileUrl());

            message.setRoom(room);

            messageRepository.save(message);

            return message;
    }
}