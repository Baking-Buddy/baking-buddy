package com.example.bakingbuddy.demo.controllers;

import com.example.bakingbuddy.demo.Model.Order;
import com.example.bakingbuddy.demo.Repos.OrderRepository;
import com.example.bakingbuddy.demo.Repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OrderController {

    @Autowired
    private OrderRepository orderDao;

    @Autowired
    private UserRepository userDao;

    @GetMapping("/orders/create")
    public String showOrderForm(Model viewModel){
        viewModel.addAttribute("order", new Order());
        return "orders/create";
    }


}
