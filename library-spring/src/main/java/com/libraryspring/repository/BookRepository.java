package com.libraryspring.repository;

import com.libraryspring.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {
    Book findById(int id);
    Book findByName(String name);
//    @Query("select b, COUNT(o.id) as count from Book b " +
//            "left join Order o on b.id=o.book_id " +
//            "where b.id = :bookId and o.status in ('ISSUED', 'WAITING') " +
//            "group by b.id")
//    Object[] findBookWithOrderCount(@Param("bookId") int bookId);
}
