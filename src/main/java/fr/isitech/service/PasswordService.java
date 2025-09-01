package fr.isitech.service;

import com.j256.ormlite.dao.Dao;
import fr.isitech.entity.PasswordEntity;
import fr.isitech.model.Password;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PasswordService {
    private Dao<PasswordEntity, Integer> passwordDao;

    public PasswordService(DatabaseHelper dbHelper) throws SQLException {
        this.passwordDao = dbHelper.getPasswordDao();
    }

    public List<Password> getAllPasswords() throws SQLException {
        List<Password> passwords = new ArrayList<>();
        for (PasswordEntity entity : passwordDao.queryForAll()) {
            Password pwd = new Password();
            pwd.id = entity.getId();
            pwd.title = entity.getTitle();
            pwd.username = entity.getUsername();
            pwd.password = entity.getPassword();
            passwords.add(pwd);
        }
        return passwords;
    }

    public void addPassword(Password password) throws SQLException {
        PasswordEntity entity = new PasswordEntity(password.title, password.username, password.password);
        passwordDao.create(entity);
    }

    public Password getPasswordById(int id) throws SQLException {
        PasswordEntity entity = passwordDao.queryForId(id);
        if (entity != null) {
            Password pwd = new Password();
            pwd.id = entity.getId();
            pwd.title = entity.getTitle();
            pwd.username = entity.getUsername();
            pwd.password = entity.getPassword();
            return pwd;
        }
        return null;
    }
}
