package service;

import dao.BookDao;
import dao.Impl.BookDaoImpl;
import entity.Book;
import lombok.AllArgsConstructor;

import java.util.List;

public class BookService {
    public static List<Book> getListBook(){
        BookDao bookDao = new BookDaoImpl();
        return bookDao.getListBook();
    }
    public static boolean addBook(Book book){
        BookDao bookDao = new BookDaoImpl();
        return bookDao.addBook(book);
    }
    public static Book findBookById(int id){
        BookDao bookDao = new BookDaoImpl();
        return bookDao.findBookById(id);
    }
    public static int countTakenBook(int id){
        BookDao bookDao = new BookDaoImpl();
        return bookDao.countTakenBook(id);
    }
    public static boolean editBook(Book book){
        BookDao bookDao = new BookDaoImpl();
        return bookDao.editBook(book);
    }
    public static boolean removeBook(int id){
        BookDao bookDao = new BookDaoImpl();
        return bookDao.removeBook(id);
    }
}
