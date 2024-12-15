package kz.dos.libraryService.dao;

import kz.dos.libraryService.models.User;
import org.springframework.beans.factory.annotation.Autowired;
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

    }

    public List<User> getAll() {
        return null;
    }

    public User getById(int id) {
        return null;
    }

    public void update(int id, User updatedUser) {

    }

    public void delete(int id) {

    }
}
