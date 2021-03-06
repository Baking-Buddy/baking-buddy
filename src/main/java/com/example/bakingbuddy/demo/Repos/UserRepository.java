package com.example.bakingbuddy.demo.Repos;

import com.example.bakingbuddy.demo.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByEmail(String email);
    User findByUsername(String username);
    User findByResetPasswordToken(String token);
    List<User> findByUsernameLike(String username);
    List<User> findAllByUsernameIsLike(String query);

    @Query(value = "FROM User u WHERE u.isBaker = true AND u.username LIKE %:query%")
    List<User> findBakerByUsernameLike(String query);

//    List<User> findAllByBake
}
