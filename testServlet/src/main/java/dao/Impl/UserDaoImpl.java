package dao.Impl;

import Connection.ConnectionPool;
import dao.UserDao;
import entity.User;
import entity.UserRole;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {

    private static final Logger log = Logger.getLogger(UserDaoImpl.class);
    private static final String ADD_USER_QUERY =
            "INSERT INTO users(username, password, email, hash, surname, address, type_auth) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String GET_ALL_USERS_QUERY =
            "SELECT users.id, username, email, surname, address FROM users";
    private static final String GET_USER_BY_EMAIL_QUERY =
            "SELECT users.id, username, password, email, role, hash, surname, address FROM users WHERE email = ? AND type_auth = ?";
    private static final String GET_USER_BY_ID_QUERY =
            "SELECT users.id, username, password, email, role, surname, address FROM users WHERE users.id = ?";
    private static final String UPDATE_USER_QUERY =
            "UPDATE users SET username = ?, email = ?, surname = ?, address = ? WHERE id = ?";
    private static final String DELETE_USER_QUERY =
            "DELETE FROM users WHERE id = ?";

    @Override
    public boolean removeUser(int id ){
        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
        try (Connection connection = connectionPool.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USER_QUERY);
            preparedStatement.setInt(1, id);
            int affectedRows = preparedStatement.executeUpdate();
            connectionPool.releaseConnection(connection);
            if (affectedRows <= 0) {
                log.error("failRemoveUser " + id);
                return false;
            }else {
                return true;
            }
        }catch (SQLException | InterruptedException e){
            log.error("removeUser : " + e);
            return false;
        }
    }
    @Override
    public boolean registerUser(User user){
        if (user == null){
            log.error("Error register");
            return false;
        }
        //add type_auth
        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
        try (Connection connection = connectionPool.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(ADD_USER_QUERY);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getHash());
            preparedStatement.setString(5, user.getSurname());
            preparedStatement.setString(6, user.getAddress());
            preparedStatement.setString(7, user.getType_auth());
            int affectedRows = preparedStatement.executeUpdate();
            connectionPool.releaseConnection(connection);
            if (affectedRows <= 0){
                log.error("failRegisterUser");
                return false;
            }else {
                return true;
            }

        }catch (SQLException | InterruptedException e){
            log.error("registerUser: " + e);
            return false;
        }
    }

    @Override
    public boolean editUser(User user){
        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();

        try (Connection connection = connectionPool.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USER_QUERY);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getSurname());
            preparedStatement.setString(4, user.getAddress());
            preparedStatement.setInt(5, user.getId());
            int affectedRows = preparedStatement.executeUpdate();
            connectionPool.releaseConnection(connection);

            if (affectedRows <= 0){
                log.error("failUpdateUser");
                return false;
            }else {
                return true;
            }
        }catch (SQLException | InterruptedException e){
            log.error("editUser : " + e);
            return false;
        }
    }
    public User getUserByEmail(String email, String type_auth){
        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();

        try (Connection connection = connectionPool.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_BY_EMAIL_QUERY);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, type_auth);
            ResultSet resultSet = preparedStatement.executeQuery();
            connectionPool.releaseConnection(connection);
            if (resultSet.next()){
                return getUserFromResultSet(resultSet);
            }

        }catch (SQLException | InterruptedException e){
            log.error("getUser : " + e);
            return null;
        }
        return null;
    }
    @Override
    public User getUser(int id){
        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
        try (Connection connection = connectionPool.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_BY_ID_QUERY);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            connectionPool.releaseConnection(connection);
            if (resultSet.next()){
                int user_id = resultSet.getInt(1);
                String username = resultSet.getString(2);
                String password = resultSet.getString(3);
                String email = resultSet.getString(4);
                UserRole role = UserRole.valueOf(resultSet.getString(5));
                String surname = resultSet.getString(6);
                String address = resultSet.getString(7);
                return new User(user_id, username, password, email, role, surname, address);
            }

        }catch (SQLException | InterruptedException e){
            log.error("getUser : " + e);
            return null;
        }
        return null;
    }

    @Override
    public List<User> getListUsers(){
        List<User> users = new ArrayList<>();
        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();

        try (Connection connection = connectionPool.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_USERS_QUERY);
            ResultSet resultSet = preparedStatement.executeQuery();
            connectionPool.releaseConnection(connection);

            while (resultSet.next()){
                int id = resultSet.getInt(1);
                String username = resultSet.getString(2);
                String email = resultSet.getString(3);
                String surname = resultSet.getString(4);
                String address = resultSet.getString(5);
                users.add(new User(id, username, email, surname, address));
            }
        }catch (SQLException | InterruptedException e){
            log.error("getListUsers: " + e);
        }
        return users;
    }

    @Override
    public User getUserFromResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt(1);
        String username = resultSet.getString(2);
        String password = resultSet.getString(3);
        String email = resultSet.getString(4);
        UserRole role = UserRole.valueOf(resultSet.getString(5));
        String hash = resultSet.getString(6);
        String surname = resultSet.getString(7);
        String address = resultSet.getString(8);
        String type_auth = null;
        return new User(id, username, password, email, role, hash, surname, address, type_auth);
    }
}
