package ru.itis.repository.impl;

import ru.itis.config.DatabaseConfig;
import ru.itis.model.ListingEntity;
import ru.itis.repository.ListingRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ListingRepositoryJdbcCrud implements ListingRepository {

    private ListingEntity map(ResultSet rs) throws SQLException {
        ListingEntity e = new ListingEntity();
        e.setId(rs.getLong("id"));
        e.setTitle(rs.getString("title"));
        e.setType(rs.getString("type"));
        e.setStatus(rs.getString("status"));
        e.setAuthorId(rs.getLong("author_id"));
        return e;
    }

    @Override
    public List<ListingEntity> findAll() {
        String sql = "select id, title, type, status, author_id from listings order by id desc";
        List<ListingEntity> list = new ArrayList<>();
        try (Connection c = DatabaseConfig.openConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(map(rs));
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("findAll failed", e);
        }
    }

    @Override
    public List<ListingEntity> findAllByAuthorId(Long userId) {
        String sql = "select id, title, type, status, author_id from listings where author_id=? order by id desc";
        List<ListingEntity> list = new ArrayList<>();
        try (Connection c = DatabaseConfig.openConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("findAllByAuthorId failed", e);
        }
    }

    @Override
    public Optional<ListingEntity> findById(Long id) {
        String sql = "select id, title, type, status, author_id from listings where id=?";
        try (Connection c = DatabaseConfig.openConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                return Optional.of(map(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("findById failed", e);
        }
    }

    @Override
    public Long create(ListingEntity e) {
        String sql = "insert into listings(title, type, status, author_id) values(?,?,?,?)";
        try (Connection c = DatabaseConfig.openConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, e.getTitle());
            ps.setString(2, e.getType());
            ps.setString(3, e.getStatus());
            ps.setLong(4, e.getAuthorId());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getLong(1);
                return null;
            }
        } catch (SQLException ex) {
            throw new RuntimeException("create failed", ex);
        }
    }

    @Override
    public void update(ListingEntity e) {
        String sql = "update listings set title=?, type=?, status=? where id=?";
        try (Connection c = DatabaseConfig.openConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, e.getTitle());
            ps.setString(2, e.getType());
            ps.setString(3, e.getStatus());
            ps.setLong(4, e.getId());
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("update failed", ex);
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "delete from listings where id=?";
        try (Connection c = DatabaseConfig.openConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("delete failed", ex);
        }
    }

    @Override
    public boolean isOwner(Long id, Long userId) {
        String sql = "select 1 from listings where id=? and author_id=? limit 1";
        try (Connection c = DatabaseConfig.openConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.setLong(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("isOwner failed", e);
        }
    }
}
