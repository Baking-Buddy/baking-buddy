package com.example.bakingbuddy.demo.Repos;

import com.example.bakingbuddy.demo.Model.Review;
import com.example.bakingbuddy.demo.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    public long getById(long id);

    List<Review> findAllByBaker(User user);
    List<Review> findAllByOwner(User user);
}
