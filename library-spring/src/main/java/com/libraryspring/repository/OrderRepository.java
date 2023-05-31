package com.libraryspring.repository;

import com.libraryspring.entity.Book;
import com.libraryspring.entity.Order;
import com.libraryspring.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    Order findById(int id);

    @Query("SELECT o FROM Order o WHERE o.user_id = :user_id AND o.book_id = :book_id")
    Order findOrderByUserIdAndBookId(@Param("user_id") User user_id, @Param("book_id") Book book_id);

    @Query("SELECT o FROM Order o WHERE o.user_id = :user_id")
    List<Order> findAllByUserId(@Param("user_id") User user_id);
    @Query("SELECT COUNT(o) from Order o where o.book_id = :book_id AND (o.status = 'ISSUED' or o.status = 'WAITING')")
    int contTakenBook(@Param("book_id") Book book);
}
