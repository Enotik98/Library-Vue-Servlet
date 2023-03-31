package servlet;

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
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        JSONObject params = TokenManager.verifyAuthorization(request);
        if (params != null) {
            if (pathInfo == null || pathInfo.equals("/")) {
                PrintWriter out = response.getWriter();
                response.setContentType("application/JSON");
                List<Book> books = BookService.getListBook();
                JSONArray array = new JSONArray(JsonUtils.getJsonString(books));
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("button", params.getString("role").equals("ADMIN"));
                jsonObject.put("books", array);
                out.println(jsonObject.toString());
            } else {
                int id = Integer.parseInt(pathInfo.substring(1));
                Book book = BookService.findBookById(id);
                if (book == null) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }
                int count = BookService.countTakenBook(id);
                JSONObject jsonObject = new JSONObject(JsonUtils.getJsonString(book));
                jsonObject.put("count", count);
                jsonObject.put("button", params.getString("role").equals("ADMIN"));
                PrintWriter out = response.getWriter();
                response.setContentType("application/JSON");
                out.println(jsonObject.toString());
            }
        }else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
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
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else {
            int id = Integer.parseInt(pathInfo.substring(1));
            book.setId(id);
            if (BookService.editBook(book)) {
                response.getWriter().write("Success Update!");
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONObject params = TokenManager.verifyAuthorization(request);
        if (params != null){
            JSONObject jsonObject = JsonUtils.getJson(request);
            Book book = JsonUtils.parseBookJsonNotId(jsonObject);
            book.setId(jsonObject.getInt("id"));
            if (BookService.editBook(book)){
                response.getWriter().write("Success Update!");
            }else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }

        }else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONObject jsonObject = JsonUtils.getJson(request);
        if (!JsonUtils.checkJsonNotEmpty(jsonObject) || !JsonUtils.checkIntValueKey(jsonObject, "id")) {
            request.setAttribute("errorMessage", "Invalid param");
            response.getWriter().write("Error Params!");
            return;
        }
        int id = jsonObject.getInt("id");
        if (BookService.removeBook(id)) {
            response.getWriter().write("Success Delete!");
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
