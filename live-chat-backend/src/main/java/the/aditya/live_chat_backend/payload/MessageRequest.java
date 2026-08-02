package the.aditya.live_chat_backend.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequest {

    private String content;
    private String sender;
    private String roomId;

    private String fileUrl;
    private String fileName;
    private String messageType;

}
