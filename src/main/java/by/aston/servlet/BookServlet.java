package by.aston.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import by.aston.controller.BookController;
import by.aston.dto.BookDto;
import by.aston.util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/books/*")
public class BookServlet extends HttpServlet {
    private BookController bookController;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        super.init();
        bookController = new BookController(/* inject BookService */);
        objectMapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            // Get all books
            List<BookDto> books = bookController.getAllBooks();
            JsonUtil.sendJsonResponse(resp, books);
        } else {
            // Get book by id
            try {
                Long id = Long.parseLong(pathInfo.substring(1));
                BookDto book = bookController.getBookById(id);
                JsonUtil.sendJsonResponse(resp, book);
            } catch (NumberFormatException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid book ID");
            } catch (RuntimeException e) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            BookDto bookDto = objectMapper.readValue(req.getInputStream(), BookDto.class);
            BookDto createdBook = bookController.createBook(bookDto);
            JsonUtil.sendJsonResponse(resp, createdBook, HttpServletResponse.SC_CREATED);
        } catch (IOException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid book data");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Book ID is required");
            return;
        }

        try {
            Long id = Long.parseLong(pathInfo.substring(1));
            BookDto bookDto = objectMapper.readValue(req.getInputStream(), BookDto.class);
            BookDto updatedBook = bookController.updateBook(id, bookDto);
            JsonUtil.sendJsonResponse(resp, updatedBook);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid book ID");
        } catch (IOException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid book data");
        } catch (RuntimeException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Book ID is required");
            return;
        }

        try {
            Long id = Long.parseLong(pathInfo.substring(1));
            bookController.deleteBook(id);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid book ID");
        }
    }
}
