package the.aditya.live_chat_backend.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OnlineUsersResponse {

    private String roomId;
    private Set<String> users;
}