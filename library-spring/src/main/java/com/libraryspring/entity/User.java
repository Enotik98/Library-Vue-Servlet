package com.libraryspring.entity;

import javax.persistence.*;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
@org.hibernate.annotations.TypeDef(name = "role", typeClass = EnumType.class)
@Data
@Entity
@Table(name = "users", schema = "public")
@NoArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @Column(name = "id", updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "username", updatable = true)
    private String username;

    @Column(name = "password", updatable = true)
    private String password;

    @Column(name = "email", updatable = true)
    private String email;

    @Enumerated(javax.persistence.EnumType.STRING)
    @Type(type = "role")
    @Column(name = "role", updatable = true)
    private Role role = Role.CLIENT;
    @Column(name = "hash", updatable = true)
    private String hash;
    @Column(name = "surname", updatable = true)
    private String surname;
    @Column(name = "address", updatable = true)
    private String address;
    @Column(name = "type_auth")
    private String type_auth;

//    @PrePersist
//    public void prePersist() {
//        if (role == null) {
//            role = Role.CLIENT; // Встановлення дефолтного значення ролі
//        }
//    }
//    public User(){
//        this.role = Users_type.CLIENT;
//    }
}
