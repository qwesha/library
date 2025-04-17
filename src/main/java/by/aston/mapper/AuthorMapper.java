package by.aston.mapper;

import by.aston.dto.AuthorDto;
import by.aston.entity.Author;
import by.aston.entity.Book;

public class AuthorMapper {
    public static AuthorDto toDto(Author author) {
        AuthorDto dto = new AuthorDto();
        dto.setId(author.getId());
        dto.setName(author.getName());
        dto.setNationality(author.getNationality());
        if (author.getBooks() != null) {
            dto.setBookIds(author.getBooks().stream().map(Book::getId).toList());
        }
        return dto;
    }

    public static Author toEntity(AuthorDto dto) {
        Author author = new Author();
        author.setId(dto.getId());
        author.setName(dto.getName());
        author.setNationality(dto.getNationality());
        return author;
    }
}