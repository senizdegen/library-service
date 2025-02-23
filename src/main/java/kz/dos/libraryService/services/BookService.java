package kz.dos.libraryService.services;

import jakarta.persistence.EntityNotFoundException;
import kz.dos.libraryService.exceptions.BookDeletionException;
import kz.dos.libraryService.models.Book;
import kz.dos.libraryService.models.User;
import kz.dos.libraryService.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Page<Book> findAllPaginated(int page, int bookPerPage) {
        Pageable pageable = PageRequest.of(page - 1, bookPerPage);
        return bookRepository.findAll(pageable);
    }

    public Page<Book> findAllPaginatedSortedByYear(int page, int bookPerPage) {
        Pageable pageable = PageRequest.of(page - 1, bookPerPage).withSort(Sort.by(Sort.Direction.ASC, "year"));
        return bookRepository.findAll(pageable);
    }

    public List<Book> findAllSortedByYear() {
        return bookRepository.findAll(Sort.by(Sort.Direction.ASC, "year"));
    }


    public Page<Book> findByNameContainingIgnoreCase(String name, int page, int booksPerPage) {
        Pageable pageable = PageRequest.of(page - 1, booksPerPage);
        return bookRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    public List<Book> findByNameContainingIgnoreCase(String name) {
        return bookRepository.findByNameContainingIgnoreCase(name);
    }


    public Book findOne(int id) {
        Optional<Book> foundBook = bookRepository.findById(id);
        return foundBook.orElse(null);
    }

    @Transactional()
    public void save(Book book) {
        bookRepository.save(book);
    }

    @Transactional
    public void update(int id, Book updatedBook) {
        updatedBook.setId(id);
        bookRepository.save(updatedBook);
    }

    @Transactional
    public void delete(int id) {
        Book book = findOne(id);
        if(book.getOwner() != null) {
            throw new BookDeletionException("The book cannot be deleted because it is currently assigned to a user.");
        }
        bookRepository.deleteById(id);
    }


    public Optional<User> getBookOwner(int id) {
        return bookRepository.findById(id)
                .map(Book::getOwner);
    }

    public void release(int id) {
        Book foundBook = bookRepository.findById(id).orElse(null);
        if (foundBook != null) {
            foundBook.setOwner(null);
            foundBook.setIssueDate(null);
            bookRepository.save(foundBook);
        }
    }

    public void assign(int id, User selectedUser) {
        System.out.println("inside service" + selectedUser);
        Book book = bookRepository.findById(id).orElse(null);
        if (book != null) {
            book.setOwner(selectedUser);
            book.setIssueDate(LocalDate.now());
            bookRepository.save(book);
        }
    }
}
