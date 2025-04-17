package by.aston.repository;

import by.aston.entity.Author;
import java.util.List;
import java.util.Optional;

public interface AuthorRepository {
    Author save(Author author);
    Optional<Author> findById(Long id);
    List<Author> findAll();
    void deleteById(Long id);
    void addBookToAuthor(Long authorId, Long bookId);
    void removeBookFromAuthor(Long authorId, Long bookId);
}