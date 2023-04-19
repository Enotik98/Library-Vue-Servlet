package dao;

import entity.Book;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface BookDao {
    boolean addBook(Book book);
    boolean editBook(Book book);
    int countTakenBook(int id);
    boolean removeBook(int id);
    List<Book> getListBook();
    Book getBook(int id);

    Book getBookFromResultSet(ResultSet resultSet) throws SQLException;
}
