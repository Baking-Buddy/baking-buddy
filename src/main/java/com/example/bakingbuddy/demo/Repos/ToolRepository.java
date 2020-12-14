package com.example.bakingbuddy.demo.Repos;

import com.example.bakingbuddy.demo.Model.Tool;
import com.example.bakingbuddy.demo.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ToolRepository extends JpaRepository<Tool, Long> {
}
