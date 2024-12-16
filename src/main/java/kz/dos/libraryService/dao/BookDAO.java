package kz.dos.libraryService.dao;

import kz.dos.libraryService.models.Book;
import kz.dos.libraryService.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class BookDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void add(Book book) {
        jdbcTemplate.update("INSERT INTO Books(name, author, year) VALUES(?,?,?)",
                book.getName(), book.getAuthor(), book.getYear());
    }

    public List<Book> getAll() {
        return jdbcTemplate.query("SELECT * FROM Books", new BeanPropertyRowMapper<>(Book.class));
    }

    public Book getById(int id) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM Books WHERE id=?",
                    new BeanPropertyRowMapper<>(Book.class), id);
        }catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    public void update(int id, Book book) {
        jdbcTemplate.update("UPDATE Books SET name=?, author=?, year=? WHERE id=?",
                book.getName(), book.getAuthor(), book.getYear(), id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM Books WHERE id=?", id);
    }

    public Optional<User> getBookOwner(int id) {
        return jdbcTemplate.query("SELECT Users.* FROM Books JOIN Users ON Users.id = Books.user_id WHERE Books.id = ?",
                new BeanPropertyRowMapper<>(User.class), id).stream().findAny();
    }

    public void release(int id){
        jdbcTemplate.update("UPDATE Books SET user_id=NULL WHERE id=?", id);
    }

    public void assign(int id, User selectedUser) {
        jdbcTemplate.update("UPDATE Books SET user_id=? WHERE id=?", selectedUser.getId(), id);
    }
}
