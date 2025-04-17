package by.aston.repository;

import by.aston.entity.Book;
import java.util.List;
import java.util.Optional;

public interface BookRepository {
    Book save(Book book);
    Optional<Book> findById(Long id);
    List<Book> findAll();
    void deleteById(Long id);
    void addAuthorToBook(Long bookId, Long authorId);
    void removeAuthorFromBook(Long bookId, Long authorId);
}
