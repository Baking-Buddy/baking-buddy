package com.example.bakingbuddy.demo.Repos;

import com.example.bakingbuddy.demo.Model.UserRole;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface Roles extends CrudRepository<UserRole, Long> {
    @Query("select ur.role from UserRole ur, User u where u.username=?1 and ur.userID = u.id")
    List<String> ofUserWith(String username);
}
