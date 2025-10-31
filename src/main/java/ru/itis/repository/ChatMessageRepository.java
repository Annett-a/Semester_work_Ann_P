package ru.itis.repository;

import java.util.List;

public interface ChatMessageRepository {
    void save(Long listingId, String messageText);
    List<String> findLast(Long listingId, int limit);
}
