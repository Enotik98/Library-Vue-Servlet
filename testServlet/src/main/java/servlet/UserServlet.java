package servlet;

import utils.JsonUtils;
import utils.TokenManager;
import entity.Order;
import entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import service.OrderService;
import service.UserService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/user/*")
public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
        //get header
        JSONObject param = TokenManager.verifyAuthorization(request);
        if (param != null) {
            //parse path and check role
            if (pathInfo == null || pathInfo.equals("/")) {
                //get all users
                if (param.getString("role").equals("ADMIN")) {
                    PrintWriter out = response.getWriter();
                    response.setContentType("application/JSON");
                    List<User> users = UserService.getListUsers();
                    out.println(JsonUtils.getJsonString(users));
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE);
                }
            } else {
                String pathParam = pathInfo.substring(1);
                if (pathParam.equals("info")) {
                    //get user
                    User user = UserService.findUserById(param.getInt("id"));
                    if (user == null) {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND);
                        return;
                    }
                    PrintWriter out = response.getWriter();
                    response.setContentType("application/JSON");
                    out.println(JsonUtils.getJsonString(user));
                }
                if (pathParam.equals("order")) {
                    List<Order> userOrders = OrderService.getOrdersByUserId(param.getInt("id"));
                    if (userOrders == null) {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND);
                        return;
                    }
                    PrintWriter out = response.getWriter();
                    response.setContentType("application/JSON");
                    out.println(JsonUtils.getJsonString(userOrders));
                }
            }
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {
        JSONObject jsonObject = JsonUtils.getJson(request);
        if (!JsonUtils.checkJsonNotEmpty(jsonObject)) {
            request.setAttribute("errorMessage", "Invalid param");
            response.getWriter().write("Error Params!");
            return;
        }
        User user = JsonUtils.parsUserJsonNotId(jsonObject);
        if (user == null) {
            request.setAttribute("errorMessage", "Invalid param");
            response.getWriter().write("Error registration ");
            return;
        }
        if (UserService.registerUser(user)) {
            response.getWriter().write("Success registration!");
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {
        JSONObject param = TokenManager.verifyAuthorization(request);

        if (param != null) {

            System.out.println("OK");

            JSONObject jsonObject = JsonUtils.getJson(request);
            User user = JsonUtils.parsUserJsonNotId(jsonObject);

            user.setId(param.getInt("id"));
            if (UserService.editUser(user)) {
                System.out.println("update");
                response.getWriter().write("Success Update!");
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        }else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }

    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {
        JSONObject params = TokenManager.verifyAuthorization(request);
//        JSONObject jsonObject = JsonUtils.getJson(request);
        if (params != null) {
//            if (!JsonUtils.checkJsonNotEmpty(jsonObject) || !JsonUtils.checkIntValueKey(jsonObject, "id")) {
//                request.setAttribute("errorMessage", "Invalid param");
//                response.getWriter().write("Error Params!");
//                return;
//            }
//            int id = jsonObject.getInt("id");
            if (UserService.removeUser(params.getInt("id"))) {
                response.getWriter().write("Success Delete!");
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        }else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
