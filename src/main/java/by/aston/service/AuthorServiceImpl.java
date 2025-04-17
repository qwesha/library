package by.aston.service;

import by.aston.dto.AuthorDto;
import by.aston.entity.Author;
import by.aston.mapper.AuthorMapper;
import by.aston.repository.AuthorRepository;

import java.util.List;
import java.util.stream.Collectors;

public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public AuthorDto create(AuthorDto authorDto) {
        Author author = AuthorMapper.toEntity(authorDto);
        Author savedAuthor = authorRepository.save(author);
        return AuthorMapper.toDto(savedAuthor);
    }

    @Override
    public AuthorDto getById(Long id) {
        return authorRepository.findById(id)
                .map(AuthorMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Author not found with id: " + id));
    }

    @Override
    public List<AuthorDto> getAll() {
        return authorRepository.findAll().stream()
                .map(AuthorMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public AuthorDto update(Long id, AuthorDto authorDto) {
        if (!authorRepository.findById(id).isPresent()) {
            throw new RuntimeException("Author not found with id: " + id);
        }

        Author author = AuthorMapper.toEntity(authorDto);
        author.setId(id);
        Author updatedAuthor = authorRepository.save(author);
        return AuthorMapper.toDto(updatedAuthor);
    }

    @Override
    public void delete(Long id) {
        authorRepository.deleteById(id);
    }

    @Override
    public void addBookToAuthor(Long authorId, Long bookId) {
        authorRepository.addBookToAuthor(authorId, bookId);
    }

    @Override
    public void removeBookFromAuthor(Long authorId, Long bookId) {
        authorRepository.removeBookFromAuthor(authorId, bookId);
    }
}
