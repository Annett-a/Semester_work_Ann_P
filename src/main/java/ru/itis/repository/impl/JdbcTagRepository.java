package ru.itis.repository.impl;

import ru.itis.config.DatabaseConfig;
import ru.itis.model.TagEntity;
import ru.itis.repository.TagRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class JdbcTagRepository implements TagRepository {

    @Override
    public List<TagEntity> findAll() {
        List<TagEntity> list = new ArrayList<>();
        String sql = "select id, name from tags order by name";
        try (Connection c = DatabaseConfig.openConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                TagEntity t = new TagEntity();
                t.setId(rs.getLong("id"));
                t.setName(rs.getString("name"));
                list.add(t);
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException("findAllTags failed", e);
        }
    }

    @Override
    public List<TagEntity> findByListingId(Long listingId) {
        List<TagEntity> list = new ArrayList<>();
        String sql = "select t.id, t.name from tags t " +
                "join listing_tags lt on lt.tag_id = t.id " +
                "where lt.listing_id = ? order by t.name";
        try (Connection c = DatabaseConfig.openConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, listingId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    TagEntity t = new TagEntity();
                    t.setId(rs.getLong("id"));
                    t.setName(rs.getString("name"));
                    list.add(t);
                }
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException("findByListingId failed", e);
        }
    }

    @Override
    public void replaceTags(Long listingId, List<Long> tagIds) {
        String del = "delete from listing_tags where listing_id = ?";
        String ins = "insert into listing_tags(listing_id, tag_id) values (?, ?)";

        try (Connection c = DatabaseConfig.openConnection()) {
            c.setAutoCommit(false);

            try (PreparedStatement d = c.prepareStatement(del)) {
                d.setLong(1, listingId);
                d.executeUpdate();
            }

            if (tagIds != null && !tagIds.isEmpty()) {
                try (PreparedStatement i = c.prepareStatement(ins)) {
                    for (Long tagId : tagIds) {
                        i.setLong(1, listingId);
                        i.setLong(2, tagId);
                        i.addBatch();
                    }
                    i.executeBatch();
                }
            }

            c.commit();
        } catch (Exception e) {
            throw new RuntimeException("replaceTags failed", e);
        }
    }
}
