package dao;

import entity.User;
import service.UserService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface UserDao {
    List<User> getListUsers();
    boolean registerUser(User user);
    boolean editUser(User user);
    User getUserFromResultSet(ResultSet resultSet) throws SQLException;
    User findUser(String username);
    User findUser(int id );
    boolean removeUser(int id );
}
