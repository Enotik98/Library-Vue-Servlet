package com.libraryspring.service;

import com.libraryspring.dto.OrderDTO;
import com.libraryspring.entity.Book;
import com.libraryspring.entity.Order;
import com.libraryspring.entity.User;
import com.libraryspring.repository.BookRepository;
import com.libraryspring.repository.OrderRepository;
import com.libraryspring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository;
    @Autowired

    UserRepository userRepository;
    @Autowired

    BookRepository bookRepository;

    public List<OrderDTO> getAllOrders (){
        List<Order> orders = orderRepository.findAll();
        List<OrderDTO> result = new ArrayList<>();
        for (Order order : orders){
            result.add(OrderDTO.ConvertToDTO(order));
        }
        return result;
    }
    public OrderDTO getOrder(int id){
        Order order = orderRepository.findById(id);
        return OrderDTO.ConvertToDTO(order);
    }
    public boolean addOrder(OrderDTO orderDTO, int userId){
        User user = userRepository.findById(userId);
        Book book = bookRepository.findById(orderDTO.getBook_id());
        Order exOrder = orderRepository.findOrderByUserIdAndBookId(user, book);
        if (exOrder != null){
            return false;
        }
        Order order = orderDTO.ConvertToOrder(user, book);
        orderRepository.save(order);

        return true;
    }
    public boolean updateOrder(OrderDTO orderDTO, int userId){
        User user = userRepository.findById(userId);
        Book book = bookRepository.findById(orderDTO.getBook_id());
        Order exOrder = orderRepository.findOrderByUserIdAndBookId(user, book);
        if (exOrder == null){
            return false;
        }
        exOrder.setType(orderDTO.getType());
        exOrder.setStatus(orderDTO.getStatus());

        orderRepository.save(exOrder);
        return true;
    }
    public boolean deleteOrder(int id){
        orderRepository.deleteById(id);
        return true;
    }
}
