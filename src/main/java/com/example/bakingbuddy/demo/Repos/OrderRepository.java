package com.example.bakingbuddy.demo.Repos;

import com.example.bakingbuddy.demo.Model.Order;
import com.example.bakingbuddy.demo.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query(value = "FROM Order WHERE owner.firstName LIKE %:query% OR owner.lastName LIKE %:query%")
    List<Order> findOwnerByNameLike(String query);

    List<Order> findAllByBaker(User user);
    List<Order> findAllByOwner(User user);
}
