package dao;

import entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface UserDao {
    List<User> getListUsers();
    boolean registerUser(User user);
    boolean editUser(User user);
    User getUserFromResultSet(ResultSet resultSet) throws SQLException;
    User getUserByEmail(String email, String type_auth);
    User getUser(int id );
    boolean removeUser(int id );
}
