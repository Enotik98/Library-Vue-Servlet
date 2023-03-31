package dao.Impl;

import Connection.ConnectionPool;
import dao.OrderDao;
import entity.Order;
import entity.StatusType;
import entity.TicketType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderDaoImpl implements OrderDao {
    private static final String UPDATE_ORDER_QUERY =
            "UPDATE orders SET user_id = ?, book_id = ?, date_order = ?, status = ?::status_order, type = ?::ticket_type WHERE id = ?";
    private static final String GET_ALL_ORDER_QUERY =
            "SELECT orders.id, user_id, book_id, date_order, status, type FROM orders";
    private static final String CREATE_ORDER_QUERY =
            "INSERT INTO orders(user_id, book_id, date_order, type) VALUES(?, ?, ?, ?::ticket_type)";
    private static final String GET_ORDER_BY_ID_QUERY =
            "SELECT orders.id, user_id, book_id, date_order, status, type FROM orders WHERE orders.id = ?";
    private static final String DELETE_ORDER_QUERY =
            "DELETE FROM orders WHERE id = ?";
    private static final String GET_ORDERS_BY_USERID_QUERY =
            "SELECT orders.id, user_id, book_id, date_order, status, type FROM orders WHERE user_id = ?";

    @Override
    public boolean removeOrder(int id) {
        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_ORDER_QUERY);
            preparedStatement.setInt(1, id);
            int affectedRows = preparedStatement.executeUpdate();
            connectionPool.releaseConnection(connection);
            if (affectedRows <= 0) {
                System.out.println("can't delete order : " + id);
                return false;
            } else {
                return true;
            }
        } catch (SQLException | InterruptedException e) {
            System.out.println("deleteOrder : " + e);
            return false;
        }
    }

    @Override
    public List<Order> getOrdersByUserId(int user_id) {
        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();

        List<Order> orders = new ArrayList<>();
        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_ORDERS_BY_USERID_QUERY);
            preparedStatement.setInt(1, user_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            connectionPool.releaseConnection(connection);

            while (resultSet.next()) {
                Order order = getOrderFromResulSet(resultSet);
                orders.add(order);
            }
            return orders;
        } catch (SQLException | InterruptedException e) {
            System.out.println("getOrderByUserId " + e);
        }
        return null;
    }

    @Override
    public Order findOrderById(int id) {
        if (id == -1) {
            System.out.println("Error findByOrder");
            return null;
        }
        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();

        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_ORDER_BY_ID_QUERY);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            connectionPool.releaseConnection(connection);
            if (resultSet.next()) {
                return getOrderFromResulSet(resultSet);
            }

        } catch (SQLException | InterruptedException e) {
            System.out.println("findOrder " + e);
        }
        return null;
    }

    @Override
    public boolean editOrder(Order order) {
        if (order == null) {
            System.out.println("Error update order");
            return false;
        }
        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();

        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_ORDER_QUERY);
            preparedStatement.setInt(1, order.getUser_id());
            preparedStatement.setInt(2, order.getBook_id());
            preparedStatement.setDate(3, (java.sql.Date) order.getDate_order());
            preparedStatement.setString(4, String.valueOf(order.getStatus()).toUpperCase());
            preparedStatement.setString(5, String.valueOf(order.getType()).toUpperCase());
            preparedStatement.setInt(6, order.getId());
            int affectedRows = preparedStatement.executeUpdate();
            connectionPool.releaseConnection(connection);

            if (affectedRows <= 0) {
                System.out.println("can't update order");
                return false;
            } else {
                return true;
            }
        } catch (SQLException | InterruptedException e) {
            System.out.println("UpdateOrder: " + e);
            return false;
        }
    }

    @Override
    public boolean createOrder(Order order) {
        if (order == null) {
            System.out.println("Error create order");
            return false;
        }
        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();

        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(CREATE_ORDER_QUERY);
            preparedStatement.setInt(1, order.getUser_id());
            preparedStatement.setInt(2, order.getBook_id());
            preparedStatement.setDate(3, (java.sql.Date) order.getDate_order());
            preparedStatement.setString(4, String.valueOf(order.getType()).toUpperCase());
            int affectedRows = preparedStatement.executeUpdate();
            connectionPool.releaseConnection(connection);

            if (affectedRows <= 0) {
                System.out.println("can't create order");
                return false;
            } else {
                return true;
            }

        } catch (SQLException | InterruptedException e) {
            System.out.println("CreateOrder: " + e);
            return false;
        }
    }

    @Override
    public List<Order> getListOrder() {
        List<Order> orders = new ArrayList<>();
        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_ORDER_QUERY);
            ResultSet resultSet = preparedStatement.executeQuery();
            connectionPool.releaseConnection(connection);
            while (resultSet.next()) {
                Order order = getOrderFromResulSet(resultSet);
                orders.add(order);
            }
        } catch (SQLException | InterruptedException e) {
            System.out.println("getListOrder " + e);
        }
        return orders;
    }

    @Override
    public Order getOrderFromResulSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt(1);
        int user_id = resultSet.getInt(2);
        int book_id = resultSet.getInt(3);
        Date date_order = resultSet.getDate(4);
        StatusType statusType = StatusType.valueOf(resultSet.getString(5));
        TicketType ticketType = TicketType.valueOf(resultSet.getString(6));
        return new Order(id, user_id, book_id, date_order, statusType, ticketType);

    }
}
