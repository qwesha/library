package by.aston.mapper;

import by.aston.dto.BookDto;
import by.aston.entity.Author;
import by.aston.entity.Book;

public class BookMapper {
    public static BookDto toDto(Book book) {
        BookDto dto = new BookDto();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setIsbn(book.getIsbn());
        dto.setPublicationYear(book.getPublicationYear());
        if (book.getAuthors() != null) {
            dto.setAuthorIds(book.getAuthors().stream().map(Author::getId).toList());
        }
        return dto;
    }

    public static Book toEntity(BookDto dto) {
        Book book = new Book();
        book.setId(dto.getId());
        book.setTitle(dto.getTitle());
        book.setIsbn(dto.getIsbn());
        book.setPublicationYear(dto.getPublicationYear());
        return book;
    }
}
