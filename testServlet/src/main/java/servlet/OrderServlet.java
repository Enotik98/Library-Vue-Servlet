package servlet;

import org.apache.log4j.Logger;
import utils.JsonUtils;
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
    private static final Logger log = Logger.getLogger(OrderServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        //get params from token
        JSONObject params = (JSONObject) request.getAttribute("params");

        if (params != null) {
            //get param in token
            if (pathInfo == null || pathInfo.equals("/")) {
                //get all orders
                if (params.getString("role").equals("ADMIN")) {
                    PrintWriter out = response.getWriter();
                    response.setContentType("application/json");
                    List<Order> orders = OrderService.getListOrders();
                    out.println(JsonUtils.getJsonString(orders));
                } else {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN);
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

        JSONObject param = (JSONObject) request.getAttribute("params");

        if (param != null) {
            JSONObject jsonOrder = JsonUtils.getJson(request);
            Order order = JsonUtils.parseOrderJsonForUpdate(jsonOrder);
            order.setUser_id(param.getInt("id"));
            order.setDate_order(new Date(System.currentTimeMillis()));
            if (OrderService.createOrder(order)) {
                response.getWriter().write("Success Create!");
                log.info("Success Create");

            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                log.info("Fail Create");

            }
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        JSONObject jsonObject = JsonUtils.getJson(request);
        JSONObject params = (JSONObject) request.getAttribute("params");
        if (params != null) {
            if ((pathInfo != null || !pathInfo.equals("/")) && params.getString("role").equals("ADMIN")) {
                Order order = JsonUtils.parseOrderJson(jsonObject);
                int id = Integer.parseInt(pathInfo.substring(1));
                order.setId(id);
                if (OrderService.editOrder(order)) {
                    response.getWriter().write("Success Update!");
                    log.info("Success Update");
                } else {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    log.info("Fail Update");
                }
            }
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }

    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONObject jsonObject = JsonUtils.getJson(request);
        JSONObject params = (JSONObject) request.getAttribute("params");
        if (!JsonUtils.checkJsonForEmptyKeys(jsonObject) || !JsonUtils.isIntValueKey(jsonObject, "id")) {
            request.setAttribute("errorMessage", "Invalid param");
            response.getWriter().write("Error Params!");
            log.info("Error Params");
            return;
        }
        if (params != null && params.getString("role").equals("ADMIN")) {
            int id = jsonObject.getInt("id");
            if (OrderService.deleteOrder(id)) {
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
