package the.aditya.live_chat_backend.service;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OnlineUserService {

    // roomId -> online users
    private final ConcurrentHashMap<String, Set<String>> roomUsers = new ConcurrentHashMap<>();

    // User Join
    public void addUser(String roomId, String username) {

        roomUsers
                .computeIfAbsent(roomId, k -> ConcurrentHashMap.newKeySet())
                .add(username);
    }

    // User Leave
    public void removeUser(String roomId, String username) {

        Set<String> users = roomUsers.get(roomId);

        if (users != null) {
            users.remove(username);

            if (users.isEmpty()) {
                roomUsers.remove(roomId);
            }
        }
    }

    // Get All Users
    public Set<String> getUsers(String roomId) {

        return roomUsers.getOrDefault(roomId, Collections.emptySet());
    }

}