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
    private final UserRole role;
    private String hash;

    public User(String username, String password, String email, UserRole role, String hash) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.hash = hash;
    }
}
