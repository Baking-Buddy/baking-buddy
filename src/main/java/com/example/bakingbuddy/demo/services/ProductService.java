package com.example.bakingbuddy.demo.services;

import com.example.bakingbuddy.demo.Model.Order;
import com.example.bakingbuddy.demo.Model.User;
import com.example.bakingbuddy.demo.Repos.OrderRepository;
import com.example.bakingbuddy.demo.Repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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

//    public List<Order> showRoleOrders(Iterable id){
//        User baker = order.getBaker();
//        return orderDao.findAllById(id);
//    }

}
