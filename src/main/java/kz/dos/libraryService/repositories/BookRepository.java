package kz.dos.libraryService.repositories;

import kz.dos.libraryService.models.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    List<Book> findByOwnerId(Integer ownerId);
    List<Book> findByNameContainingIgnoreCase(String name);
    Page<Book> findByNameContainingIgnoreCase(String name, Pageable pageable);
    List<Book> findAll(Sort sort);
    Page<Book> findByYear(Integer year, Pageable pageable);
}
