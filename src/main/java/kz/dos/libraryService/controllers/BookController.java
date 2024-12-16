package kz.dos.libraryService.controllers;

import kz.dos.libraryService.dao.BookDAO;
import kz.dos.libraryService.models.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/books")
public class BookController {
    private final BookDAO bookDAO;

    @Autowired
    public BookController(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }

    @GetMapping
    public String getAllBooks(Model model) {
        model.addAttribute("books", bookDAO.getAll());

        return "books/all";
    }

    @GetMapping("/{id}")
    public String getBookById(@PathVariable("id") int id, Model model) {
        model.addAttribute("book", bookDAO.getById(id));

        return "books/single";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "books/new";
    }

    @PostMapping
    public String createBook(@ModelAttribute("book") Book book) {
        bookDAO.add(book);

        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String editBook(@PathVariable("id") int id, Model model) {
        model.addAttribute("book", bookDAO.getById(id));

        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String updateBook(@ModelAttribute("book") Book book, @PathVariable("id") int id) {
        bookDAO.update(id, book);

        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable("id") int id) {
        bookDAO.delete(id);

        return "redirect:/books";
    }
}
