package by.aston.service;

import by.aston.dto.AuthorDto;
import java.util.List;

public interface AuthorService {
    AuthorDto create(AuthorDto authorDto);
    AuthorDto getById(Long id);
    List<AuthorDto> getAll();
    AuthorDto update(Long id, AuthorDto authorDto);
    void delete(Long id);
    void addBookToAuthor(Long authorId, Long bookId);
    void removeBookFromAuthor(Long authorId, Long bookId);
}