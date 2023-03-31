package entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Order {
    private int id;
    private int user_id;
    private int book_id;
    private Date date_order;
    private StatusType status;
    private final TicketType type;

    public Order( int book_id, TicketType type) {
        this.book_id = book_id;
        this.type = type;
    }

    public Order(int user_id, int book_id, Date date_order, StatusType status, TicketType type) {
        this.user_id = user_id;
        this.book_id = book_id;
        this.date_order = date_order;
        this.status = status;
        this.type = type;
    }
}
