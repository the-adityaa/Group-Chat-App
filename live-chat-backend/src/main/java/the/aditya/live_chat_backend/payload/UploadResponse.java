package the.aditya.live_chat_backend.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UploadResponse {

    private String fileUrl;
    private String fileName;
    private String messageType;
}