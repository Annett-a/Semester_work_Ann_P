package ru.itis.service.impl;

import ru.itis.repository.ChatMessageRepository;
import ru.itis.service.RoomChatService;
import java.util.List;

public class JdbcRoomChatService implements RoomChatService {
    private final ChatMessageRepository repo;
    private final int cap;

    public JdbcRoomChatService(ChatMessageRepository repo, int capacity) {
        this.repo = repo;
        this.cap = Math.max(1, capacity);
    }

    @Override
    public void add(Long roomId, String message) {
        if (roomId == null || message == null || message.isBlank()) return;
        repo.save(roomId, message);
    }

    @Override
    public List<String> last(Long roomId, int limit) {
        if (roomId == null) return java.util.List.of();
        return repo.findLast(roomId, Math.min(limit, cap));
    }

    @Override
    public int capacity() {
        return cap;
    }
}
