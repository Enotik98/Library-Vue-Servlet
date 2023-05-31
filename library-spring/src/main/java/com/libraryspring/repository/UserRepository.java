package com.libraryspring.repository;

import com.libraryspring.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findById(int id);
    @Query("SELECT u FROM User u WHERE u.email = :email AND u.type_auth = :type_auth")
    User findByEmailAndType_auth(@Param("email") String email, @Param("type_auth") String type_auth);


}
