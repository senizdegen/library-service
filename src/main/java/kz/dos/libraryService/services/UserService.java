package kz.dos.libraryService.services;

import kz.dos.libraryService.exceptions.UserDeletionException;
import kz.dos.libraryService.models.Book;
import kz.dos.libraryService.models.User;
import kz.dos.libraryService.repositories.BookRepository;
import kz.dos.libraryService.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly=true)
public class UserService {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    @Autowired
    public UserService(UserRepository userRepository, BookRepository bookRepository) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findOne(int id) {
        Optional<User> foundUser = userRepository.findById(id);

        return foundUser.orElse(null);
    }

    @Transactional()
    public void save(User user) {
        userRepository.save(user);
    }

    @Transactional()
    public void update(int id, User updatedUser) {
        updatedUser.setId(id);
        userRepository.save(updatedUser);
    }

    @Transactional()
    public void delete(int id) {
        List<Book> books = getBooksByUserId(id);
        if(books != null && !books.isEmpty()) {
            throw new UserDeletionException("Cannot delete user with outstanding books. " +
                    "Please return all books before deletion.");
        }
        userRepository.deleteById(id);
    }

    public List<Book> getBooksByUserId(int userId) {
        return bookRepository.findByOwnerId(userId);
    }

    public Optional<User> getByFullName(String fullName) {
        return userRepository.findByFullName(fullName);
    }
}
