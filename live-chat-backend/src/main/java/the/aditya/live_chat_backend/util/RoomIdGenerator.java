package the.aditya.live_chat_backend.util;

import java.security.SecureRandom;

public class RoomIdGenerator {

    private static final String CHARACTERS =
            "abcdefghijklmnopqrstuvwxyz0123456789";

    private static final SecureRandom random = new SecureRandom();

    public static String generateRoomId() {

        StringBuilder roomId = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            roomId.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }

        return roomId.toString();
    }
}