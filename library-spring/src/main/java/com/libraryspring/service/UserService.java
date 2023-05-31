package com.libraryspring.service;

import com.libraryspring.dto.UserDTO;
import com.libraryspring.entity.Order;
import com.libraryspring.entity.User;
import com.libraryspring.repository.OrderRepository;
import com.libraryspring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    OrderRepository orderRepository;

    public User loadUserLogin(String email, String type_auth){
        User user = userRepository.findByEmailAndType_auth(email, type_auth);
        if (user == null){
            System.out.println("NOT FOUND");
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }
    public boolean addUser(UserDTO userDTO){
        User existingUser = userRepository.findByEmailAndType_auth(userDTO.getEmail(), userDTO.getType_auth());
        if (existingUser != null){
            return false;
        }
        User user = userDTO.ConvertToUser();
        userRepository.save(user);
        return true;
    }
    public List<UserDTO> getAllUsers(){
        List<User> users = userRepository.findAll();
        List<UserDTO> result = new ArrayList<>();
        for (User user : users){
            result.add(UserDTO.ConvertToDTO(user));
        }
        return result;
    }
    public UserDTO getUser(int id){
        User user = userRepository.findById(id);
        return UserDTO.ConvertToDTO(user);
    }
    public List<Order> getUserOrders(int userId){
        User user = userRepository.findById(userId);
        return orderRepository.findAllByUserId(user);
    }
    public boolean updateUser(UserDTO userDTO, int userId){
        User existingUser = userRepository.findById(userId);
        if (existingUser == null){
            return false;
        }
        User user = userDTO.ConvertToUser();
        user.setId(userId);
        user.setRole(existingUser.getRole());

        userRepository.save(user);
        return true;
    }
    public boolean deleteUser(int id){
        userRepository.deleteById(id);
        return true;
    }
}
