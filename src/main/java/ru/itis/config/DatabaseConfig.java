package ru.itis.config;

import lombok.experimental.UtilityClass;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@UtilityClass
public class DatabaseConfig {
    private static final PGSimpleDataSource DS = new PGSimpleDataSource();
    static {
        // ПОДСТАВЬ свою БД/логин/пароль
        DS.setURL("jdbc:postgresql://localhost:5432/semester");
        DS.setUser("postgres");
        DS.setPassword("3a33144");
    }

    public static DataSource dataSource() { return DS; }

    // Оставим старое имя, чтобы всё компилировалось
    public static Connection openConnection() throws SQLException { return DS.getConnection(); }

    // И совместимость с теми, кто вызывает getConnection()
    public static Connection getConnection() throws SQLException { return openConnection(); }
}
