package dao;

import entity.Order;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface OrderDao {
    Order getOrderFromResulSet(ResultSet resultSet) throws SQLException;
    Order findOrderById(int id);
    boolean removeOrder(int id);
    List<Order> getListOrder();
    List<Order> getOrdersByUserId(int user_id);
    boolean createOrder(Order order);

    boolean editOrder(Order order);
}
