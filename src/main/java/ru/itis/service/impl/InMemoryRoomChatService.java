package ru.itis.service.impl;

import ru.itis.service.RoomChatService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryRoomChatService implements RoomChatService {
    private final int cap;
    private final Map<Long, Deque<String>> rooms = new ConcurrentHashMap<>();

    public InMemoryRoomChatService(int capacity) {
        this.cap = Math.max(1, capacity);
    }

    @Override
    public void add(Long roomId, String msg) {
        if (roomId == null) return;
        rooms.computeIfAbsent(roomId, k -> new ArrayDeque<>());
        Deque<String> q = rooms.get(roomId);
        synchronized (q) {
            q.addLast(msg);
            while (q.size() > cap) q.removeFirst();
        }
    }

    @Override
    public List<String> last(Long roomId, int limit) {
        if (roomId == null) return List.of();
        Deque<String> q = rooms.get(roomId);
        if (q == null) return List.of();
        int n = Math.min(limit, q.size());
        List<String> out = new ArrayList<>(n);
        int skip = q.size() - n;
        int i = 0;
        synchronized (q) {
            for (String s : q) {
                if (i++ < skip) continue;
                out.add(s);
            }
        }
        return out;
    }

    @Override
    public int capacity() {
        return cap;
    }
}
