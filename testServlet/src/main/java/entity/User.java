package entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class User {
    private int id;
    private String username;
    private String password;
    private String email;
    private UserRole role;
    private String hash;
    private String surname;
    private String address;

    public User(int id, String username, String email, String surname, String address) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.surname = surname;
        this.address = address;
    }

    public User(String username, String password, String email, String hash, String surname, String address) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.hash = hash;
        this.surname = surname;
        this.address = address;
    }

    public User(String username, String password, String email, UserRole role, String hash, String surname, String address) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.hash = hash;
        this.surname = surname;
        this.address = address;
    }
}
