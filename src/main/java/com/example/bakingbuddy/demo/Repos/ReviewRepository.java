package com.example.bakingbuddy.demo.Repos;

import com.example.bakingbuddy.demo.Model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    public long getById(long id);

}
