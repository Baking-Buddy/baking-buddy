package com.example.bakingbuddy.demo.Repos;

import com.example.bakingbuddy.demo.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
