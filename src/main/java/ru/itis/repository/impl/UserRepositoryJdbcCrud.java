package ru.itis.repository.impl;

import ru.itis.config.DatabaseConfig;
import ru.itis.model.UserEntity;
import ru.itis.repository.UserRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;

public class UserRepositoryJdbcCrud implements UserRepository {

    @Override
    public Optional<UserEntity> findByEmail(String email) {
        String sql = "select id, email, password, full_name from users where email=?";
        try (Connection c = DatabaseConfig.openConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                UserEntity u = new UserEntity();
                u.setId(rs.getLong("id"));
                u.setEmail(rs.getString("email"));
                u.setPassword(rs.getString("password"));
                u.setFullName(rs.getString("full_name"));
                return Optional.of(u);
            }

        } catch (Exception e) {
            throw new RuntimeException("findByEmail failed", e);
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        String sql = "select 1 from users where email=? limit 1";
        try (Connection c = DatabaseConfig.openConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (Exception e) {
            throw new RuntimeException("existsByEmail failed", e);
        }
    }

    @Override
    public void create(UserEntity u) {
        String sql = "insert into users(email,password,full_name) values(?,?,?)";
        try (Connection c = DatabaseConfig.openConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, u.getEmail());
            ps.setString(2, u.getPassword());
            ps.setString(3, u.getFullName());
            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("create user failed", e);
        }
    }
}
