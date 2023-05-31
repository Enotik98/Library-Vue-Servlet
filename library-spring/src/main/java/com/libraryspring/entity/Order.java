package com.libraryspring.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@org.hibernate.annotations.TypeDef(name = "status_order", typeClass = EnumType.class)
@org.hibernate.annotations.TypeDef(name = "ticket_type", typeClass = EnumType.class)

@Data
@Entity
@Table(name = "orders", schema = "public")
@NoArgsConstructor
@Getter
@Setter
public class Order {
    @Id
    @Column(name = "id", updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user_id;
    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private Book book_id;
    @Column(name = "date_order", columnDefinition = "DATE DEFAULT CURRENT_DATE")
    private Date date_order = new Date(System.currentTimeMillis());
    @Enumerated(javax.persistence.EnumType.STRING)
    @Type(type = "status_order")
    @Column(name = "status")
    private StatusOrder status = StatusOrder.WAITING;
    @Enumerated(javax.persistence.EnumType.STRING)
    @Type(type = "ticket_type")
    @Column(name = "type")
    private TicketType type;

}
