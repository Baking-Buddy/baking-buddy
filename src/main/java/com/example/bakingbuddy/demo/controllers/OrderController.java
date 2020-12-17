package com.example.bakingbuddy.demo.controllers;

import com.example.bakingbuddy.demo.Model.Consumable;
import com.example.bakingbuddy.demo.Model.Order;
import com.example.bakingbuddy.demo.Model.OrderStatus;
import com.example.bakingbuddy.demo.Model.User;
import com.example.bakingbuddy.demo.Repos.OrderRepository;
import com.example.bakingbuddy.demo.Repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
public class OrderController {

    @Autowired
    private OrderRepository orderDao;

    @Autowired
    private UserRepository userDao;


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
        User userDb = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        orderToBeSaved.setOwner(userDb);
        orderToBeSaved.setStatus(OrderStatus.PENDING);
        Order dbOrder = orderDao.save(orderToBeSaved);
        return "redirect:/orders/" + dbOrder.getId();
    }

    @GetMapping("/orders")
    public String showOrders(Model model){
        model.addAttribute("orders",orderDao.findAll());
        return "orders/orders";
    }

    @GetMapping("/orders/{id}/edit")
    public String editOrderForm(@PathVariable long id, Model model){
        model.addAttribute("order", orderDao.getOne(id));
        return "orders/edit-order";
    }

    @PostMapping("/orders/{id}/edit")
    public String submitOrderEdit(@PathVariable long id, @RequestParam(name = "description") String newDescription, @RequestParam(name = "date") Date newDate) {
        Order order = orderDao.getOne(id);
//        Date convertedDate = newDate;
        order.setDescription(newDescription);
        order.setDate(newDate);
        orderDao.save(order);
        return "redirect:/orders";
    }



}
