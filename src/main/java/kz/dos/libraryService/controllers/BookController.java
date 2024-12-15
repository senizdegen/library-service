package kz.dos.libraryService.controllers;

import kz.dos.libraryService.dao.BookDAO;
import kz.dos.libraryService.models.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/books")
public class BookController {
    private final BookDAO bookDAO;

    @Autowired
    public BookController(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }

    @GetMapping
    public String getAllBooks() {
        return "books/all";
    }

    @GetMapping("/{id}")
    public String getBookById(int id) {
        return "books/single";
    }

    @GetMapping("/new")
    public String newBook() {
        return "books/new";
    }

    public String createBook(@ModelAttribute("book") Book book) {
        bookDAO.add(book);

        return "redirect:/books";
    }
}
