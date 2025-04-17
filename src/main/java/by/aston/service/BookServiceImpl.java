package by.aston.service;

import by.aston.dto.BookDto;
import by.aston.entity.Book;
import by.aston.mapper.BookMapper;
import by.aston.repository.BookRepository;

import java.util.List;
import java.util.stream.Collectors;

public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public BookDto create(BookDto bookDto) {
        Book book = BookMapper.toEntity(bookDto);
        Book savedBook = bookRepository.save(book);
        return BookMapper.toDto(savedBook);
    }

    @Override
    public BookDto getById(Long id) {
        return bookRepository.findById(id)
                .map(BookMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));
    }

    @Override
    public List<BookDto> getAll() {
        return bookRepository.findAll().stream()
                .map(BookMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public BookDto update(Long id, BookDto bookDto) {
        if (!bookRepository.findById(id).isPresent()) {
            throw new RuntimeException("Book not found with id: " + id);
        }

        Book book = BookMapper.toEntity(bookDto);
        book.setId(id);
        Book updatedBook = bookRepository.save(book);
        return BookMapper.toDto(updatedBook);
    }

    @Override
    public void delete(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public void addAuthorToBook(Long bookId, Long authorId) {
        bookRepository.addAuthorToBook(bookId, authorId);
    }

    @Override
    public void removeAuthorFromBook(Long bookId, Long authorId) {
        bookRepository.removeAuthorFromBook(bookId, authorId);
    }
}
