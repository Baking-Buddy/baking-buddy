package com.example.bakingbuddy.demo.services;

import com.example.bakingbuddy.demo.Model.Order;
import com.example.bakingbuddy.demo.Repos.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private OrderRepository orderDao;

    public List<Order> listAll(String query) {
        if(query != null) {
            return orderDao.findOwnerByNameLike(query);
        }
        return orderDao.findAll();
    }
}
