package kz.dos.libraryService.dao;

import kz.dos.libraryService.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void add(User user) {
        jdbcTemplate.update("INSERT INTO Users(fullName, birthYear) VALUES (?, ?)",
                user.getFullName(), user.getBirthYear());
    }

    public List<User> getAll() {
        return jdbcTemplate.query("SELECT * FROM Users", new BeanPropertyRowMapper<>(User.class));
    }

    public User getById(int id) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM Users WHERE id = ?",
                    new BeanPropertyRowMapper<>(User.class),
                    id
            );

        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


    public void update(int id, User updatedUser) {
        jdbcTemplate.update("UPDATE Users SET fullName=?, birthYear=? WHERE id=?",
                updatedUser.getFullName(), updatedUser.getBirthYear(), id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM Users WHERE id=?", id);
    }
}
