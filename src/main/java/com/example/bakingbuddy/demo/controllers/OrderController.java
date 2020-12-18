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
//        sessionUser is pulled and put within userDao to keep most updated information.
        User sessionUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userDb = userDao.getOne(sessionUser.getId());
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
    public String submitOrderEdit(@ModelAttribute Order orderToBeEdited) {
        User userDb = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        orderToBeEdited.setOwner(userDb);
        orderToBeEdited.setStatus(OrderStatus.PENDING);
        orderDao.save(orderToBeEdited);
        return "redirect:/orders";
    }

    @PostMapping("/accept")
    public String acceptOrder(@ModelAttribute Order orderToBeAccepted){
        User sessionUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userDb = userDao.getOne(sessionUser.getId());
        if(userDb.isBaker()){
            orderToBeAccepted.setBaker(userDb);
            orderToBeAccepted.setStatus(OrderStatus.ACCEPTED);
        }
        return "redirect:/orders";
    }


}
