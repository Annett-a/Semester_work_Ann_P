package ru.itis.repository.impl;

import ru.itis.config.DatabaseConfig;
import ru.itis.repository.ChatMessageRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ChatMessageRepositoryJdbc implements ChatMessageRepository {

    @Override
    public void save(Long listingId, String messageText) {
        String sql = "insert into chat_messages(listing_id, message_text) values(?, ?)";
        try (Connection c = DatabaseConfig.openConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, listingId);
            ps.setString(2, messageText);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("save chat message failed", e);
        }
    }

    @Override
    public List<String> findLast(Long listingId, int limit) {
        String sql = """
            select message_text
            from chat_messages
            where listing_id=?
            order by created_at desc, id desc
            limit ?
            """;
        List<String> out = new ArrayList<>();
        try (Connection c = DatabaseConfig.openConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, listingId);
            ps.setInt(2, Math.max(1, limit));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) out.add(rs.getString("message_text"));
            }
        } catch (Exception e) {
            throw new RuntimeException("findLast chat messages failed", e);
        }
        java.util.Collections.reverse(out);
        return out;
    }
}
