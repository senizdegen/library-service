package kz.dos.libraryService.controllers;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import kz.dos.libraryService.exceptions.BookDeletionException;
import kz.dos.libraryService.models.Book;
import kz.dos.libraryService.models.User;
import kz.dos.libraryService.services.BookService;
import kz.dos.libraryService.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;
    private final UserService userService;

    @Autowired
    public BookController(BookService bookService, UserService userService) {
        this.bookService = bookService;
        this.userService = userService;
    }

//    @GetMapping
//    public String getAllBooks(Model model,
//                              @RequestParam(name = "page", defaultValue = "1") int page,
//                              @RequestParam(name = "booksPerPage", defaultValue = "0") int booksPerPage) {
//
//        System.out.println("page = " + page);
//        System.out.println("booksPerPage = " + booksPerPage);
//
//        List<Book> books;
//        int totalPages = 1;
//
//        if (booksPerPage > 0) {
//            Page<Book> bookPage = bookService.findAllPaginated(page, booksPerPage);
//            books = bookPage.getContent();
//            totalPages = bookPage.getTotalPages();
//        } else {
//            books = bookService.findAll();
//        }
//
//
//        // Передача значений в модель
//        model.addAttribute("books", books);
//        model.addAttribute("currentPage", page);
//        model.addAttribute("totalPages", totalPages);
//        model.addAttribute("booksPerPage", booksPerPage);
//
//        System.out.println("currentPage = " + page);
//        System.out.println("totalPages = " + totalPages);
//        System.out.println("booksPerPage = " + booksPerPage);
//
//        return "books/all";
//    }

    @GetMapping
    public String getBooks(
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "booksPerPage", required = false) Integer booksPerPage,
            @RequestParam(name = "pagination", defaultValue = "false") boolean pagination,
            Model model) {

        List<Book> books;
        int totalPages = 1;

        if (!pagination) {
            books = bookService.findAll();
        } else {
            page = (page == null) ? 1 : page;
            booksPerPage = (booksPerPage == null) ? 10 : booksPerPage;
            Page<Book> bookPage = bookService.findAllPaginated(page, booksPerPage);
            books = bookPage.getContent();
            totalPages = bookPage.getTotalPages();
        }
        model.addAttribute("books", books);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("booksPerPage", booksPerPage);
        model.addAttribute("paginationEnabled", pagination);

        return "books/all";
    }



    @GetMapping("/{id}")
    public String getBookById(@PathVariable("id") int id, Model model, @ModelAttribute("user") User user) {
        model.addAttribute("book", bookService.findOne(id));

        Optional<User> bookOwner = bookService.getBookOwner(id);
        if(bookOwner.isPresent()) {
            model.addAttribute("owner", bookOwner.get());
        }else{
            model.addAttribute("users", userService.findAll());
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

        bookService.save(book);

        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String editBook(@PathVariable("id") int id, Model model) {
        model.addAttribute("book", bookService.findOne(id));

        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String updateBook(@ModelAttribute("book") @Valid Book book, @PathVariable("id") int id, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "books/edit";
        }

        bookService.update(id, book);

        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable("id") int id, Model model) {
        try {
            bookService.delete(id);
            return "redirect:/books";
        } catch (BookDeletionException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "books/error";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "An unexpected error occurred: " + e.getMessage());
            return "books/error";
        }
    }


    @PatchMapping("/{id}/release")
    public String releaseBook(@PathVariable("id") int id) {
        bookService.release(id);
        return "redirect:/books/" + id;
    }

    @PatchMapping("/{id}/assign")
    public String assignBook(@PathVariable("id") int id, @RequestParam("id") int userId) {
        System.out.println("userId = " + userId);
        User selectedUser = userService.findOne(userId);
        bookService.assign(id, selectedUser);
        return "redirect:/books/" + id;
    }
}
