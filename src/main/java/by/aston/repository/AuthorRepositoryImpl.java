package by.aston.repository;

import by.aston.config.DatabaseConfig;
import by.aston.entity.Author;
import by.aston.entity.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AuthorRepositoryImpl implements AuthorRepository {
    @Override
    public Author save(Author author) {
        String sql = author.getId() == null ?
                "INSERT INTO authors (name, nationality) VALUES (?, ?)" :
                "UPDATE authors SET name = ?, nationality = ? WHERE id = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, author.getName());
            statement.setString(2, author.getNationality());

            if (author.getId() != null) {
                statement.setLong(3, author.getId());
            }

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating author failed, no rows affected.");
            }

            if (author.getId() == null) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        author.setId(generatedKeys.getLong(1));
                    } else {
                        throw new SQLException("Creating author failed, no ID obtained.");
                    }
                }
            }

            return author;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving author", e);
        }
    }

    @Override
    public Optional<Author> findById(Long id) {
        String sql = "SELECT * FROM authors WHERE id = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Author author = new Author();
                author.setId(resultSet.getLong("id"));
                author.setName(resultSet.getString("name"));
                author.setNationality(resultSet.getString("nationality"));

                // Load books
                List<Book> books = findBooksByAuthorId(id);
                author.setBooks(books);

                return Optional.of(author);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding author by id", e);
        }

        return Optional.empty();
    }

    @Override
    public List<Author> findAll() {
        String sql = "SELECT * FROM authors";
        List<Author> authors = new ArrayList<>();

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Author author = new Author();
                author.setId(resultSet.getLong("id"));
                author.setName(resultSet.getString("name"));
                author.setNationality(resultSet.getString("nationality"));
                authors.add(author);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all authors", e);
        }

        return authors;
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM authors WHERE id = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting author", e);
        }
    }

    @Override
    public void addBookToAuthor(Long authorId, Long bookId) {
        String sql = "INSERT INTO author_book (author_id, book_id) VALUES (?, ?)";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, authorId);
            statement.setLong(2, bookId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error adding book to author", e);
        }
    }

    @Override
    public void removeBookFromAuthor(Long authorId, Long bookId) {
        String sql = "DELETE FROM author_book WHERE author_id = ? AND book_id = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, authorId);
            statement.setLong(2, bookId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error removing book from author", e);
        }
    }

    private List<Book> findBooksByAuthorId(Long authorId) {
        String sql = "SELECT b.* FROM books b JOIN author_book ab ON b.id = ab.book_id WHERE ab.author_id = ?";
        List<Book> books = new ArrayList<>();

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, authorId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Book book = new Book();
                book.setId(resultSet.getLong("id"));
                book.setTitle(resultSet.getString("title"));
                book.setIsbn(resultSet.getString("isbn"));
                book.setPublicationYear(resultSet.getInt("publication_year"));
                books.add(book);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding books by author id", e);
        }

        return books;
    }
}
