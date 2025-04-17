package by.aston.repository;


import by.aston.config.DatabaseConfig;
import by.aston.entity.Author;
import by.aston.entity.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookRepositoryImpl implements BookRepository {
    @Override
    public Book save(Book book) {
        String sql = book.getId() == null ?
                "INSERT INTO books (title, isbn, publication_year) VALUES (?, ?, ?)" :
                "UPDATE books SET title = ?, isbn = ?, publication_year = ? WHERE id = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, book.getTitle());
            statement.setString(2, book.getIsbn());
            statement.setInt(3, book.getPublicationYear());

            if (book.getId() != null) {
                statement.setLong(4, book.getId());
            }

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating book failed, no rows affected.");
            }

            if (book.getId() == null) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        book.setId(generatedKeys.getLong(1));
                    } else {
                        throw new SQLException("Creating book failed, no ID obtained.");
                    }
                }
            }

            return book;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving book", e);
        }
    }

    @Override
    public Optional<Book> findById(Long id) {
        String sql = "SELECT * FROM books WHERE id = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Book book = new Book();
                book.setId(resultSet.getLong("id"));
                book.setTitle(resultSet.getString("title"));
                book.setIsbn(resultSet.getString("isbn"));
                book.setPublicationYear(resultSet.getInt("publication_year"));

                // Load authors
                List<Author> authors = findAuthorsByBookId(id);
                book.setAuthors(authors);

                return Optional.of(book);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding book by id", e);
        }

        return Optional.empty();
    }

    @Override
    public List<Book> findAll() {
        String sql = "SELECT * FROM books";
        List<Book> books = new ArrayList<>();

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Book book = new Book();
                book.setId(resultSet.getLong("id"));
                book.setTitle(resultSet.getString("title"));
                book.setIsbn(resultSet.getString("isbn"));
                book.setPublicationYear(resultSet.getInt("publication_year"));
                books.add(book);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all books", e);
        }

        return books;
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM books WHERE id = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting book", e);
        }
    }

    @Override
    public void addAuthorToBook(Long bookId, Long authorId) {
        String sql = "INSERT INTO author_book (book_id, author_id) VALUES (?, ?)";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, bookId);
            statement.setLong(2, authorId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error adding author to book", e);
        }
    }

    @Override
    public void removeAuthorFromBook(Long bookId, Long authorId) {
        String sql = "DELETE FROM author_book WHERE book_id = ? AND author_id = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, bookId);
            statement.setLong(2, authorId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error removing author from book", e);
        }
    }

    private List<Author> findAuthorsByBookId(Long bookId) {
        String sql = "SELECT a.* FROM authors a JOIN author_book ab ON a.id = ab.author_id WHERE ab.book_id = ?";
        List<Author> authors = new ArrayList<>();

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, bookId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Author author = new Author();
                author.setId(resultSet.getLong("id"));
                author.setName(resultSet.getString("name"));
                author.setNationality(resultSet.getString("nationality"));
                authors.add(author);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding authors by book id", e);
        }

        return authors;
    }
}