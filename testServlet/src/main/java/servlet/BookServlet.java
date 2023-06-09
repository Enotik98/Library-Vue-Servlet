package servlet;

import org.apache.log4j.Logger;
import utils.JsonUtils;
import entity.Book;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import service.BookService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/book/*")
public class BookServlet extends HttpServlet {
    private static final Logger log = Logger.getLogger(BookServlet.class);
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        PrintWriter out = response.getWriter();
        response.setContentType("application/JSON");
        JSONObject jsonObject = new JSONObject();
        if (pathInfo == null || pathInfo.equals("/")) {
            List<Book> books = BookService.getListBook();
            JSONArray array = new JSONArray(JsonUtils.getJsonString(books));
            jsonObject.put("books", array);
        } else {
            int id = Integer.parseInt(pathInfo.substring(1));
            Book book = BookService.findBookById(id);
            if (book == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            int count = BookService.countTakenBook(id);
//            jsonObject = new JSONObject();
            jsonObject.put("book", new JSONObject(JsonUtils.getJsonString(book)));
            jsonObject.put("count", count);
        }
        out.println(jsonObject.toString());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        JSONObject params = (JSONObject) request.getAttribute("params");

        JSONObject jsonObject = JsonUtils.getJson(request);
        if (!JsonUtils.checkJsonForEmptyKeys(jsonObject)){
            request.setAttribute("errorMessage", "Invalid param");
            response.getWriter().write("Error Params!");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            log.info("Error Params");
            return;
        }

        Book book = JsonUtils.parseBookJson(jsonObject);
        if (params != null && (pathInfo == null || pathInfo.equals("/")) && params.getString("role").equals("ADMIN")) {
            if (BookService.addBook(book)) {
                response.getWriter().write("Success Create!");
                log.info("Success Create");
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                log.info("Fail Create");

            }
        }else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONObject params = (JSONObject) request.getAttribute("params");
        if (params != null && params.getString("role").equals("ADMIN")) {
            JSONObject jsonObject = JsonUtils.getJson(request);
            if (!JsonUtils.checkJsonForEmptyKeys(jsonObject)){
                request.setAttribute("errorMessage", "Invalid param");
                response.getWriter().write("Error Params!");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                log.info("Error Params");
                return;
            }
            Book book = JsonUtils.parseBookJson(jsonObject);
            book.setId(jsonObject.getInt("id"));
            if (BookService.editBook(book)) {
                response.getWriter().write("Success Update!");
                log.info("Success Update");
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                log.info("Fail Update");
            }
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONObject params = (JSONObject) request.getAttribute("params");
        JSONObject jsonObject = JsonUtils.getJson(request);
        if (params.getString("role").equals("ADMIN")) {
            if (!JsonUtils.checkJsonForEmptyKeys(jsonObject) || !JsonUtils.isIntValueKey(jsonObject, "id")) {
                request.setAttribute("errorMessage", "Invalid param");
                response.getWriter().write("Error Params!");
                log.info("Error Params");
                return;
            }
            int id = jsonObject.getInt("id");
            if (BookService.removeBook(id)) {
                response.getWriter().write("Success Delete!");
                log.info("Success Delete");
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                log.info("Fail Delete");
            }
        }else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Access denied. Admin access required.");
        }
    }
}
