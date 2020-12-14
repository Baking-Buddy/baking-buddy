package com.example.bakingbuddy.demo.controllers;

import com.example.bakingbuddy.demo.Model.Order;
import com.example.bakingbuddy.demo.Model.User;
import com.example.bakingbuddy.demo.Repos.OrderRepository;
import com.example.bakingbuddy.demo.Repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class OrderController {

    @Autowired
    private OrderRepository orderDao;

    @Autowired
    private UserRepository userDao;

//    public OrderController(OrderRepository orderDao, UserRepository userDao){
//        this.orderDao = orderDao;
//        this.userDao = userDao;
//    }

    @GetMapping("/orders/{id}")
    public String order(@PathVariable long id, Model viewModel){
        viewModel.addAttribute("order", orderDao.getOne(id));
        return "orders/customer-order";
    }

    @GetMapping("/orders/create")
    public String showOrderForm(Model viewModel){
        viewModel.addAttribute("order", new Order());
        return "orders/create";
    }

    @PostMapping("/orders/create")
    public String createOrder(@ModelAttribute Order orderToBeSaved){
        User userDb = userDao.getOne(1L);
        orderToBeSaved.setOwner(userDb);
        Order dbOrder = orderDao.save(orderToBeSaved);
        return "redirect:/orders/" + dbOrder.getId();
    }


}
