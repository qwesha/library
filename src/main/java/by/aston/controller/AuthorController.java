package by.aston.controller;

import by.aston.dto.AuthorDto;
import by.aston.service.AuthorService;

import java.util.List;

public class AuthorController {
    private final AuthorService authorService = null;

    public AuthorController() {
    }

    public AuthorDto createAuthor(AuthorDto authorDto) {
        return authorService.create(authorDto);
    }

    public AuthorDto getAuthorById(Long id) {
        return authorService.getById(id);
    }

    public List<AuthorDto> getAllAuthors() {
        return authorService.getAll();
    }

    public AuthorDto updateAuthor(Long id, AuthorDto authorDto) {
        return authorService.update(id, authorDto);
    }

    public void deleteAuthor(Long id) {
        authorService.delete(id);
    }

    public void addBookToAuthor(Long authorId, Long bookId) {
        authorService.addBookToAuthor(authorId, bookId);
    }

    public void removeBookFromAuthor(Long authorId, Long bookId) {
        authorService.removeBookFromAuthor(authorId, bookId);
    }
}
