package com.example.bakingbuddy.demo.services;

import com.example.bakingbuddy.demo.Model.Order;
import com.example.bakingbuddy.demo.Model.User;
import com.example.bakingbuddy.demo.Repos.OrderRepository;
import com.example.bakingbuddy.demo.Repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    private OrderRepository orderDao;
    @Autowired
    private UserRepository userDao;

    public List<Order> listAllBaker(String query, User user) {
        if(query != null) {
            return orderDao.findOwnerByNameLike(query);
        }
        return orderDao.findAllByBaker(user);
    }

//    public List<Order> showRoleOrders(Iterable id){
//        User baker = order.getBaker();
//        return orderDao.findAllById(id);
//    }

    public List<Order> bakerOrdersProfile(Long bakerID){
        List<Order> approvedOrders = new ArrayList<>();
        List<Order> startingList = orderDao.findAllByBaker(userDao.getOne(bakerID));
        for (Order order : startingList){
            if (order.getStatus().toString().equals("ACCEPTED")){
                approvedOrders.add(order);
            }
        }
        return approvedOrders;
    }

    public List<Order> listAllOwner(String query, User user) {
        if(query != null) {
            return orderDao.findOwnerByNameLike(query);
        }
        return orderDao.findAllByOwner(user);
    }

}
