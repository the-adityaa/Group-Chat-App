package the.aditya.live_chat_backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Setter;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Message {

    @Id
    private String id;

    private String sender;
    private String content;
    private LocalDateTime timeStamp;

    private String fileUrl;
    private String fileName;
    private String messageType;

    @ManyToOne
    @JoinColumn(name = "room_id")
    @JsonIgnore
    private Room room;

}

