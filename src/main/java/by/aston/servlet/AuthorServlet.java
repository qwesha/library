package by.aston.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import by.aston.controller.AuthorController;
import by.aston.dto.AuthorDto;
import by.aston.util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/authors/*")
public class AuthorServlet extends HttpServlet {
    private AuthorController authorController;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        super.init();
        authorController = new AuthorController(/* inject AuthorService */);
        objectMapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            // Get all authors
            List<AuthorDto> authors = authorController.getAllAuthors();
            JsonUtil.sendJsonResponse(resp, authors);
        } else {
            // Get author by id
            try {
                Long id = Long.parseLong(pathInfo.substring(1));
                AuthorDto author = authorController.getAuthorById(id);
                JsonUtil.sendJsonResponse(resp, author);
            } catch (NumberFormatException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid author ID");
            } catch (RuntimeException e) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            AuthorDto authorDto = objectMapper.readValue(req.getInputStream(), AuthorDto.class);
            AuthorDto createdAuthor = authorController.createAuthor(authorDto);
            JsonUtil.sendJsonResponse(resp, createdAuthor, HttpServletResponse.SC_CREATED);
        } catch (IOException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid author data");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Author ID is required");
            return;
        }

        try {
            Long id = Long.parseLong(pathInfo.substring(1));
            AuthorDto authorDto = objectMapper.readValue(req.getInputStream(), AuthorDto.class);
            AuthorDto updatedAuthor = authorController.updateAuthor(id, authorDto);
            JsonUtil.sendJsonResponse(resp, updatedAuthor);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid author ID");
        } catch (IOException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid author data");
        } catch (RuntimeException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Author ID is required");
            return;
        }

        try {
            Long id = Long.parseLong(pathInfo.substring(1));
            authorController.deleteAuthor(id);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid author ID");
        }
    }
}
