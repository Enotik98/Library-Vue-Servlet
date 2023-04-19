package service;

import dao.Impl.OrderDaoImpl;
import dao.OrderDao;
import entity.Order;

import java.util.List;

public class OrderService {
    public static List<Order> getListOrders(){
        OrderDao orderDao = new OrderDaoImpl();
        return orderDao.getListOrder();
    }
    public static List<Order> getOrdersByUserId(int user_id){
        OrderDao orderDao = new OrderDaoImpl();
        return orderDao.getOrdersByUserId(user_id);
    }
    public static boolean createOrder(Order order){
        OrderDao orderDao = new OrderDaoImpl();
        return orderDao.addOrder(order);
    }
    public static boolean editOrder(Order order){
        OrderDao orderDao = new OrderDaoImpl();
        return orderDao.editOrder(order);
    }
    public static Order findOrderById(int id){
        OrderDao orderDao = new OrderDaoImpl();
        return orderDao.getOrderById(id);
    }
    public static boolean deleteOrder(int id){
        OrderDao orderDao = new OrderDaoImpl();
        return orderDao.removeOrder(id);
    }
}
