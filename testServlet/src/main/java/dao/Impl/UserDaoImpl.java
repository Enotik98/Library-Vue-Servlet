package dao.Impl;

import Connection.ConnectionPool;
import dao.UserDao;
import entity.User;
import entity.UserRole;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {

    private static final String ADD_USER_QUERY =
            "INSERT INTO users(username, password, email, role, hash) VALUES (?, ?, ?, ?::users_type, ?)";
    private static final String GET_ALL_USERS_QUERY =
            "SELECT users.id, username, password, email, role, hash FROM users";
    private static final String CHECK_USER_BY_USERNAME_QUERY =
            "SELECT users.id, username, password, email, role, hash FROM users WHERE username = ?";
    private static final String CHECK_USER_BY_ID_QUERY =
            "SELECT users.id, username, password, email, role, hash FROM users WHERE users.id = ?";
    private static final String UPDATE_USER_QUERY =
            "UPDATE users SET username = ?, password = ?, email = ?, role = ?::users_type, hash = ? WHERE id = ?";
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
                System.out.println("can't delete user : " + id);
                return false;
            }else {
                return true;
            }
        }catch (SQLException | InterruptedException e){
            System.out.println("removeUser : " + e);
            return false;
        }
    }
    @Override
    public boolean registerUser(User user){
        if (user == null){
            System.out.println("Error register");
            return false;
        }
        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
        try (Connection connection = connectionPool.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(ADD_USER_QUERY);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, String.valueOf(user.getRole()).toUpperCase());
            preparedStatement.setString(5, user.getHash());
            int affectedRows = preparedStatement.executeUpdate();
            connectionPool.releaseConnection(connection);
            if (affectedRows <= 0){
                System.out.println("can't register user");
                return false;
            }else {
                return true;
            }

        }catch (SQLException | InterruptedException e){
            System.out.println("registerUser: " + e);
            return false;
        }
    }

    @Override
    public boolean editUser(User user){
        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();

        try (Connection connection = connectionPool.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USER_QUERY);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, String.valueOf(user.getRole()).toUpperCase());
            preparedStatement.setString(5, user.getHash());
            preparedStatement.setInt(6, user.getId());
            int affectedRows = preparedStatement.executeUpdate();
            connectionPool.releaseConnection(connection);

            if (affectedRows <= 0){
                System.out.println("can't register user");
                return false;
            }else {
                return true;
            }
        }catch (SQLException | InterruptedException e){
            System.out.println("editUser : " + e);
            return false;
        }
    }
    @Override
    public User findUser(String username){
        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();

        try (Connection connection = connectionPool.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(CHECK_USER_BY_USERNAME_QUERY);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            connectionPool.releaseConnection(connection);
            if (resultSet.next()){
                return getUserFromResultSet(resultSet);
            }

        }catch (SQLException | InterruptedException e){
            System.out.println("fU : " + e);
            return null;
        }
        return null;
    }
    @Override
    public User findUser(int id){
        ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
        try (Connection connection = connectionPool.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(CHECK_USER_BY_ID_QUERY);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            connectionPool.releaseConnection(connection);
            if (resultSet.next()){
                return getUserFromResultSet(resultSet);
            }

        }catch (SQLException | InterruptedException e){
            System.out.println("fU : " + e);
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
                User user = getUserFromResultSet(resultSet);
                users.add(user);
            }
        }catch (SQLException | InterruptedException e){
            System.out.println("getListUsers: " + e);
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
        return new User(id, username, password, email, role, hash);
    }
}
