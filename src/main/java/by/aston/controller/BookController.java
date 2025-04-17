package by.aston.controller;

import by.aston.dto.BookDto;
import by.aston.service.BookService;

import java.util.List;


public class BookController {
    private final BookService bookService = null;

    public BookDto createBook(BookDto bookDto) {
        return bookService.create(bookDto);
    }

    public BookDto getBookById(Long id) {
        return bookService.getById(id);
    }

    public List<BookDto> getAllBooks() {
        return bookService.getAll();
    }

    public BookDto updateBook(Long id, BookDto bookDto) {
        return bookService.update(id, bookDto);
    }

    public void deleteBook(Long id) {
        bookService.delete(id);
    }

    public void addAuthorToBook(Long bookId, Long authorId) {
        bookService.addAuthorToBook(bookId, authorId);
    }

    public void removeAuthorFromBook(Long bookId, Long authorId) {
        bookService.removeAuthorFromBook(bookId, authorId);
    }
}
