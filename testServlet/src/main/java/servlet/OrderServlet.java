package servlet;

import utils.JsonUtils;
import utils.TokenManager;
import entity.Order;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import service.OrderService;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.List;

@WebServlet("/order/*")
public class OrderServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        //get header and verify accessToken
        JSONObject param = TokenManager.verifyAuthorization(request);
        if (param != null) {
            //get param in token
            if (pathInfo == null || pathInfo.equals("/")) {
                //get all orders
                if (param.getString("role").equals("ADMIN")) {
                    PrintWriter out = response.getWriter();
                    response.setContentType("application/json");
                    List<Order> orders = OrderService.getListOrders();
                    out.println(JsonUtils.getJsonString(orders));
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE);
                }
            } else {
                int id = Integer.parseInt(pathInfo.substring(1));
                Order order = OrderService.findOrderById(id);
                if (order == null) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }
                PrintWriter out = response.getWriter();
                response.setContentType("application/json");
                out.println(JsonUtils.getJsonString(order));
            }
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        JSONObject param = TokenManager.verifyAuthorization(request);
        if (param != null) {
            System.out.println("Order OK");
            JSONObject jsonOrder = JsonUtils.getJson(request);
            Order order = JsonUtils.parseOrderJsonNotId(jsonOrder);
            order.setUser_id(param.getInt("id"));
            order.setDate_order(new Date(System.currentTimeMillis()));
            if (OrderService.createOrder(order)) {
                response.getWriter().write("Success Create!");
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        JSONObject jsonObject = JsonUtils.getJson(request);
        JSONObject params = TokenManager.verifyAuthorization(request);
        if (params != null) {
            if ((pathInfo != null || !pathInfo.equals("/")) && params.getString("role").equals("ADMIN")) {
                Order order = JsonUtils.parseOrderJsonNotIdWithStatus(jsonObject);
                int id = Integer.parseInt(pathInfo.substring(1));
                order.setId(id);
                if (OrderService.editOrder(order)) {
                    response.getWriter().write("Success Update!");
                } else {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                }
            }
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }

    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONObject jsonObject = JsonUtils.getJson(request);
        JSONObject params = TokenManager.verifyAuthorization(request);
        if (!JsonUtils.checkJsonNotEmpty(jsonObject) || !JsonUtils.checkIntValueKey(jsonObject, "id")) {
            request.setAttribute("errorMessage", "Invalid param");
            response.getWriter().write("Error Params!");
            return;
        }
        if (params != null) {
            int id = jsonObject.getInt("id");
            if (OrderService.deleteOrder(id)) {
                response.getWriter().write("Success Delete!");
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        }else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
