package com.example.bakingbuddy.demo.Repos;

import com.example.bakingbuddy.demo.Model.Tool;
import com.example.bakingbuddy.demo.Model.ToolImage;
import com.example.bakingbuddy.demo.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ToolImageRepository extends JpaRepository<ToolImage, Long> {
    ToolImage findToolImageByTool(Tool tool);
}
