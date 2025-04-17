package by.aston.service;

import by.aston.dto.BookDto;
import java.util.List;

public interface BookService {
    BookDto create(BookDto bookDto);
    BookDto getById(Long id);
    List<BookDto> getAll();
    BookDto update(Long id, BookDto bookDto);
    void delete(Long id);
    void addAuthorToBook(Long bookId, Long authorId);
    void removeAuthorFromBook(Long bookId, Long authorId);
}
