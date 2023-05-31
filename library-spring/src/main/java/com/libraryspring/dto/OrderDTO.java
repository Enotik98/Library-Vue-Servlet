package com.libraryspring.dto;

import com.libraryspring.entity.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class OrderDTO {
    private int id;
    private int user_id;
    private int book_id;
    private Date date_order;
    private StatusOrder status;
    private TicketType type;

    public static OrderDTO ConvertToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setUser_id(order.getUser_id().getId());
        dto.setBook_id(order.getBook_id().getId());
        dto.setDate_order(order.getDate_order());
        dto.setStatus(order.getStatus());
        dto.setType(order.getType());
        return dto;
    }

    public Order ConvertToOrder(User user, Book book) {
        Order order = new Order();
        order.setId(this.id);
        order.setUser_id(user);
        order.setBook_id(book);
//        order.setDate_order(this.getDate_order());
        if (this.getStatus() != null) {
            order.setStatus(this.status);
        }
        order.setType(this.type);
        return order;
    }
}
