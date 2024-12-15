package kz.dos.libraryService.dao;

import kz.dos.libraryService.models.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void add(Book book) {

    }

    public List<Book> getAll() {
        return null;
    }

    public Book getById(int id) {
        return null;
    }

    public void update(Book book) {

    }

    public void delete(int id) {

    }
}
