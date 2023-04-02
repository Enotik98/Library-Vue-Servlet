package service;

import dao.Impl.UserDaoImpl;
import dao.UserDao;
import entity.User;
import entity.UserRole;
import lombok.AllArgsConstructor;

import java.security.MessageDigest;
import java.util.Base64;
import java.util.List;

public class UserService {

    public static User findUserByEmail(String email){
        UserDao userDao = new UserDaoImpl();
        return userDao.findUser(email);
    }
    public static User findUserById(int id){
        UserDao userDao = new UserDaoImpl();
        return userDao.findUser(id);
    }
    public static boolean editUser(User user){
        UserDao userDao = new UserDaoImpl();
        return userDao.editUser(user);
    }
    public static List<User> getListUsers(){
        UserDao userDao = new UserDaoImpl();
        return userDao.getListUsers();
    }
    public static boolean registerUser(User user){
        UserDao userDao = new UserDaoImpl();
        return userDao.registerUser(user);
    }
    public static boolean removeUser(int id){
        UserDao userDao = new UserDaoImpl();
        return userDao.removeUser(id);
    }
    public static String hasPassword(String password){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedByte = digest.digest(password.getBytes("UTF-8"));
            String hashedPassword = Base64.getEncoder().encodeToString(hashedByte);
            return hashedPassword;
        }catch (Exception e){
            System.out.println("hash :" + e);
            return null;
        }
    }


    @AllArgsConstructor
    public static class UserInfo{
        public int id;
        public UserRole type;
        public String cash;
    }
}
