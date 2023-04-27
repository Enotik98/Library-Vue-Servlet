package servlet;

import org.apache.log4j.Logger;
import utils.JsonUtils;
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
    private static final Logger log = Logger.getLogger(UserServlet.class.getName());
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
        //get params from token
        JSONObject params = (JSONObject) request.getAttribute("params");
        if (params != null) {
            //parse path and check role
            if (pathInfo == null || pathInfo.equals("/")) {
                //get all users
                if (params.getString("role").equals("ADMIN")) {
                    PrintWriter out = response.getWriter();
                    response.setContentType("application/JSON");
                    List<User> users = UserService.getListUsers();
                    out.println(JsonUtils.getJsonString(users));
                } else {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN);
                    response.getWriter().write("Access denied. Admin access required.");
                }
            } else {
                String pathParam = pathInfo.substring(1);
                if (pathParam.equals("info")) {
                    //get user info
                    User user = UserService.findUserById(params.getInt("id"));
                    if (user == null) {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND);
                        return;
                    }
                    PrintWriter out = response.getWriter();
                    response.setContentType("application/JSON");
                    out.println(JsonUtils.getJsonString(user));
                }
                if (pathParam.equals("order")) {
                    //get user order
                    List<Order> userOrders = OrderService.getOrdersByUserId(params.getInt("id"));
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
        User user = JsonUtils.parseUserJson(jsonObject);
        if (user == null) {
            request.setAttribute("errorMessage", "Invalid param");
            response.getWriter().write("Error registration ");
            log.info("doPost Error Registration");
            return;
        }
        if (UserService.registerUser(user)) {
            response.getWriter().write("Success registration!");
            log.info("Success registration");
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {
        JSONObject param = (JSONObject) request.getAttribute("params");

        if (param != null) {
            JSONObject jsonObject = JsonUtils.getJson(request);
            User user = JsonUtils.parseUserJson(jsonObject);

            user.setId(param.getInt("id"));
            if (UserService.editUser(user)) {
                System.out.println("update");
                response.getWriter().write("Success Update!");
                log.info("Success Update!");
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                log.info("Fail Update");
            }
        }else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }

    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {
        JSONObject params = (JSONObject) request.getAttribute("params");

        if (params != null ) {
            if (UserService.removeUser(params.getInt("id"))) {
                response.getWriter().write("Success Delete!");
                log.info("Success Delete");
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                log.info("Fail Delete");
            }
        }else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
