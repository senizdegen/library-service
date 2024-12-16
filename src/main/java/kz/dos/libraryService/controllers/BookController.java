package kz.dos.libraryService.controllers;

import jakarta.validation.Valid;
import kz.dos.libraryService.dao.BookDAO;
import kz.dos.libraryService.dao.UserDAO;
import kz.dos.libraryService.models.Book;
import kz.dos.libraryService.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BookController {
    private final BookDAO bookDAO;
    private final UserDAO userDAO;

    @Autowired
    public BookController(BookDAO bookDAO, UserDAO userDAO) {
        this.bookDAO = bookDAO;
        this.userDAO = userDAO;
    }

    @GetMapping
    public String getAllBooks(Model model) {
        model.addAttribute("books", bookDAO.getAll());

        return "books/all";
    }

    @GetMapping("/{id}")
    public String getBookById(@PathVariable("id") int id, Model model, @ModelAttribute("user") User user) {
        model.addAttribute("book", bookDAO.getById(id));

        Optional<User> bookOwner = bookDAO.getBookOwner(id);
        if(bookOwner.isPresent()) {
            model.addAttribute("owner", bookOwner.get());
        }else{
            model.addAttribute("users", userDAO.getAll());
        }

        return "books/single";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "books/new";
    }

    @PostMapping
    public String createBook(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            return "books/new";
        }

        bookDAO.add(book);

        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String editBook(@PathVariable("id") int id, Model model) {
        model.addAttribute("book", bookDAO.getById(id));

        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String updateBook(@ModelAttribute("book") @Valid Book book, @PathVariable("id") int id, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "books/edit";
        }

        bookDAO.update(id, book);

        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable("id") int id) {
        bookDAO.delete(id);

        return "redirect:/books";
    }

    @PatchMapping("/{id}/release")
    public String releaseBook(@PathVariable("id") int id) {
        bookDAO.release(id);
        return "redirect:/books/" + id;
    }

    @PatchMapping("/{id}/assign")
    public String assignBook(@PathVariable("id") int id, @ModelAttribute("user") User selectedUser) {
        bookDAO.assign(id, selectedUser);
        return "redirect:/books/" + id;
    }
}
