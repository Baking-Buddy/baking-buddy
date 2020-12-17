package com.example.bakingbuddy.demo.Repos;

import com.example.bakingbuddy.demo.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    List<User> findByUsernameLike(String username);
    List<User> findAllByUsernameIsLike(String query);

}
