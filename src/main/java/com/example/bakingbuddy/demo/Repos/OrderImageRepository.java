package com.example.bakingbuddy.demo.Repos;

import com.example.bakingbuddy.demo.Model.OrderImage;
import com.example.bakingbuddy.demo.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderImageRepository extends JpaRepository<OrderImage, Long> {
}
