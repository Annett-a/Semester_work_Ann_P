package ru.itis.service;

import java.util.List;

public interface RoomChatService {
    void add(Long roomId, String message);
    List<String> last(Long roomId, int limit);
    int capacity();
}
