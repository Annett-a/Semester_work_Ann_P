package ru.itis.repository.impl;

import ru.itis.config.DatabaseConfig;
import ru.itis.model.PhotoEntity;
import ru.itis.repository.PhotoRepository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PhotoRepositoryJdbc implements PhotoRepository {
    private PhotoEntity map(ResultSet rs) throws SQLException {
        PhotoEntity p = new PhotoEntity();
        p.setId(rs.getLong("id"));
        p.setListingId(rs.getLong("listing_id"));
        p.setFileName(rs.getString("file_name"));
        p.setStoragePath(rs.getString("storage_path"));
        p.setContentType(rs.getString("content_type"));
        p.setSize(rs.getLong("size"));
        return p;
    }

    @Override
    public Long create(PhotoEntity p) {
        String sql = "insert into public.listing_photos(listing_id,file_name,storage_path,content_type,size) " +
                "values (?,?,?,?,?)";
        try (Connection c = DatabaseConfig.openConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, p.getListingId());
            ps.setString(2, p.getFileName());
            ps.setString(3, p.getStoragePath());
            ps.setString(4, p.getContentType());
            ps.setLong(5, p.getSize());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                return keys.next() ? keys.getLong(1) : null;
            }
        } catch (Exception e) {
            throw new RuntimeException("photo create failed", e);
        }
    }

    @Override
    public List<PhotoEntity> findByListingId(Long listingId) {
        String sql = "select id, listing_id, file_name, storage_path, content_type, size " +
                "from public.listing_photos where listing_id=? order by id";
        List<PhotoEntity> list = new ArrayList<>();
        try (Connection c = DatabaseConfig.openConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, listingId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException("photo findByListingId failed", e);
        }
    }

    @Override
    public Optional<PhotoEntity> findById(Long id) {
        String sql = "select id, listing_id, file_name, storage_path, content_type, size " +
                "from public.listing_photos where id=?";
        try (Connection c = DatabaseConfig.openConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                return Optional.of(map(rs));
            }
        } catch (Exception e) {
            throw new RuntimeException("photo findById failed", e);
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "delete from public.listing_photos where id=?";
        try (Connection c = DatabaseConfig.openConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("photo delete failed", e);
        }
    }
}
