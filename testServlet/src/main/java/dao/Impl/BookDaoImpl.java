package dao.Impl;

import Connection.ConnectionPool;
import dao.BookDao;
import entity.Book;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookDaoImpl implements BookDao {
    private static final Logger log = Logger.getLogger(BookDaoImpl.class);
    private static final String ADD_BOOK_QUERY =
            "INSERT INTO books(name, author, genre, quantity, year) VALUES(?, ?, ?, ?, ?)";
    private static final String GET_ALL_BOOKS_QUERY =
            "SELECT id, name, author, genre, quantity, year FROM books";
    private static final String CHECK_BOOK_QUERY =
            "SELECT id, name, author, genre, quantity, year FROM books WHERE id = ?";
    private static final String COUNT_BOOK_QUERY =
            "SELECT Count(*) From orders Where book_id = ? AND (status = 'ISSUED' OR status = 'WAITING')";
    //    private static final String COUNT_BOOK_QUERY =
//            "SELECT Count(*) From orders Join books On orders.book_id = books.id Where books.name = ? AND orders.status = 'ISSUED'";
    private static final String UPDATE_BOOK_QUERY =
            "UPDATE books SET name = ?, author = ?, genre = ?, quantity = ?, year = ? WHERE id = ?";
    private static final String DELETE_BOOK_QUERY =
            "DELETE FROM books WHERE id = ?";

    @Override
    public boolean removeBook(int id) {
        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BOOK_QUERY);
            preparedStatement.setInt(1, id);
            int affectedRows = preparedStatement.executeUpdate();
            connectionPool.releaseConnection(connection);
            if (affectedRows <= 0) {
                log.error("can't delete book");
                return false;
            } else {
                return true;
            }
        } catch (SQLException | InterruptedException e) {
            log.error("removeBook: " + e);
            return false;
        }
    }


    @Override
    public boolean editBook(Book book) {
        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();

        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_BOOK_QUERY);
            preparedStatement.setString(1, book.getName());
            preparedStatement.setString(2, book.getAuthor());
            preparedStatement.setString(3, book.getGenre());
            preparedStatement.setInt(4, book.getQuantity());
            preparedStatement.setInt(5, book.getYear());
            preparedStatement.setInt(6, book.getId());
            int affectedRows = preparedStatement.executeUpdate();
            connectionPool.releaseConnection(connection);

            if (affectedRows <= 0) {
                log.error("can't update book");
                return false;
            } else {
                return true;
            }
        } catch (SQLException | InterruptedException e) {
            log.error("editBook: " + e);
            return false;
        }
    }


    @Override
    public int countTakenBook(int id) {
        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();

        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(COUNT_BOOK_QUERY);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            connectionPool.releaseConnection(connection);
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count;
            }

        } catch (SQLException | InterruptedException e) {
            log.error("countTakenBook: " + e);
        }
        return -1;
    }

    @Override
    public boolean addBook(Book book) {
        if (book == null) {
            log.error("Error add book");
            return false;
        }
        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(ADD_BOOK_QUERY);
            preparedStatement.setString(1, book.getName());
            preparedStatement.setString(2, book.getAuthor());
            preparedStatement.setString(3, book.getGenre());
            preparedStatement.setInt(4, book.getQuantity());
            preparedStatement.setInt(5, book.getYear());
            int affectedRows = preparedStatement.executeUpdate();
            connectionPool.releaseConnection(connection);
            if (affectedRows <= 0) {
                log.error("can't add book");
                return false;
            } else {
                return true;
            }
        } catch (SQLException | InterruptedException e) {
            log.error("addBook: " + e);
            return false;
        }
    }

    @Override
    public Book findBookById(int id) {
        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();

        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(CHECK_BOOK_QUERY);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            connectionPool.releaseConnection(connection);
            if (resultSet.next()) {
                Book book = getBookFromResultSet(resultSet);
                return book;
            }

        } catch (SQLException | InterruptedException e) {
            log.error("fb: " + e);
        }
        return null;
    }

    @Override
    public List<Book> getListBook() {
        List<Book> books = new ArrayList<>();
        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_BOOKS_QUERY);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Book book = getBookFromResultSet(resultSet);
                books.add(book);
            }
            connectionPool.releaseConnection(connection);

        } catch (SQLException | InterruptedException e) {
            log.error("getListBook: " + e);
        }

        return books;
    }

    public Book getBookFromResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt(1);
        String name = resultSet.getString(2);
        String author = resultSet.getString(3);
        String genre = resultSet.getString(4);
        int quantity = resultSet.getInt(5);
        int year = resultSet.getInt(6);
        return new Book(id, name, author, genre, quantity, year);
    }
}
