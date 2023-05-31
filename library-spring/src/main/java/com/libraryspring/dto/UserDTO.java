package com.libraryspring.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.libraryspring.entity.User;
import com.libraryspring.entity.Role;
import com.libraryspring.utils.PasswordUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDTO {
    private int id;
    private String username;
    private String password;
    private String email;
    private Role role;
    private String hash;
    private String surname;
    private String address;
    private String type_auth;

    public static UserDTO ConvertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
//        dto.setPassword(user.getPassword());
        dto.setEmail(user.getEmail());
//        dto.setHash(user.getHash());
        dto.setSurname(user.getSurname());
        dto.setAddress(user.getAddress());
//        dto.setType_auth(user.getType_auth());
        dto.setRole(user.getRole());
        return dto;
    }
    public User ConvertToUser(){
        User user = new User();
//        user.setId(userDTO.getId());
        user.setUsername(this.username);
        user.setEmail(this.email);
        if (this.password != null) {
            user.setPassword(this.password);
            user.setHash(PasswordUtils.hashPassword(user.getPassword()));
        }
        user.setSurname(this.getSurname());
        user.setAddress(this.getAddress());
        if (this.type_auth != null) {
            user.setType_auth(this.getType_auth());
        }
//        user.setRole(userDTO.getRole());
        return user;
    }
}
