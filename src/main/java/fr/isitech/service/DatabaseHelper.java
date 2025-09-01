package fr.isitech.service;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import fr.isitech.entity.PasswordEntity;

import java.sql.SQLException;

public class DatabaseHelper {
    private static final String DATABASE_URL = "jdbc:sqlite:passwords.db";
    private ConnectionSource connectionSource;
    private Dao<PasswordEntity, Integer> passwordDao;

    public DatabaseHelper() throws SQLException {
        connectionSource = new JdbcConnectionSource(DATABASE_URL);
        passwordDao = DaoManager.createDao(connectionSource, PasswordEntity.class);
        TableUtils.createTableIfNotExists(connectionSource, PasswordEntity.class);
    }

    public Dao<PasswordEntity, Integer> getPasswordDao() {
        return passwordDao;
    }

    public void close() throws Exception {
        connectionSource.close();
    }
}
