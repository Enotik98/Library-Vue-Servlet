package servlet;

import org.apache.log4j.Logger;
import utils.JsonUtils;
import utils.TokenManager;
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
//        JSONObject params = TokenManager.verifyAuthorization(request);
        JSONObject params = (JSONObject) request.getAttribute("params");
        PrintWriter out = response.getWriter();
        response.setContentType("application/JSON");
        JSONObject jsonObject = new JSONObject();
        if (params != null) {
            jsonObject.put("button", params.getString("role").equals("ADMIN"));
        }else {
            jsonObject.put("button", false);
        }
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
            jsonObject = new JSONObject(JsonUtils.getJsonString(book));
            jsonObject.put("count", count);
//            jsonObject.put("button", params.getString("role").equals("ADMIN"));
        }
        out.println(jsonObject.toString());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        JSONObject jsonObject = JsonUtils.getJson(request);
        Book book = JsonUtils.parseBookJsonNotId(jsonObject);
        if (pathInfo == null || pathInfo.equals("/")) {
            //create
            if (BookService.addBook(book)) {
                response.getWriter().write("Success Create!");
                log.info("Success Create");
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                log.info("Fail Create");

            }
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONObject params = TokenManager.verifyAuthorization(request);
        if (params != null) {
            JSONObject jsonObject = JsonUtils.getJson(request);
            Book book = JsonUtils.parseBookJsonNotId(jsonObject);
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
        JSONObject jsonObject = JsonUtils.getJson(request);
        if (!JsonUtils.checkJsonNotEmpty(jsonObject) || !JsonUtils.checkIntValueKey(jsonObject, "id")) {
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
    }
}
