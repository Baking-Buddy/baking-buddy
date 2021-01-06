package com.example.bakingbuddy.demo.controllers;

import com.example.bakingbuddy.demo.Model.Consumable;
import com.example.bakingbuddy.demo.Model.Order;
import com.example.bakingbuddy.demo.Model.OrderImage;
import com.example.bakingbuddy.demo.Model.OrderStatus;
import com.example.bakingbuddy.demo.Model.User;
import com.example.bakingbuddy.demo.Repos.OrderImageRepository;
import com.example.bakingbuddy.demo.Repos.OrderRepository;
import com.example.bakingbuddy.demo.Repos.UserRepository;
import com.example.bakingbuddy.demo.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.Date;
import java.util.List;

@Controller
public class OrderController {

    @Autowired
    private OrderRepository orderDao;

    @Autowired
    private UserRepository userDao;

    @Autowired
    private OrderImageRepository orderImageDao;


    @Autowired
    private ProductService service;



    @GetMapping("/orders/{id}")
    public String order(@PathVariable long id, Model viewModel){
        viewModel.addAttribute("order", orderDao.getOne(id));
        return "orders/customer-order";
    }


    @GetMapping("/orders/create/{id}")
    public String showOrderForm(Model viewModel, @PathVariable long id){
        viewModel.addAttribute("order", new Order());
        viewModel.addAttribute("bakerID", id);
        return "orders/create";
    }

    @PostMapping("/orders/create/{id}")
    public String createOrder(@ModelAttribute Order orderToBeSaved, @RequestParam(name="uploadedImage") String uploadedImage, @PathVariable long id){
        User sessionUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userDb = userDao.getOne(sessionUser.getId());

        orderToBeSaved.setBaker(userDao.getOne(id));
        orderToBeSaved.setOwner(userDb);
        orderToBeSaved.setStatus(OrderStatus.PENDING);
        Order dbOrder = orderDao.save(orderToBeSaved);

        OrderImage orderImage = new OrderImage(uploadedImage, dbOrder);
        orderImageDao.save(orderImage);
        return "redirect:/orders/" + dbOrder.getId();
    }


    @GetMapping("/orders")
    public String showOrders(Model model){
        User sessionUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userDb = userDao.getOne(sessionUser.getId());
        if(userDb.isBaker()){
            model.addAttribute("orders", orderDao.findAllByBaker(userDb));
            model.addAttribute("user", userDb);
        }else if(!userDb.isBaker()){
            model.addAttribute("orders", orderDao.findAllByOwner(userDb));
            model.addAttribute("user", userDb);
        }

//        model.addAttribute("orders",orderDao.findAll());
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

    @PostMapping("/accept/{id}")
    public String acceptOrder(@PathVariable long id){
        Order orderToAccept = orderDao.getOne(id);
        orderToAccept.setStatus(OrderStatus.ACCEPTED);
        orderDao.save(orderToAccept);
        return "redirect:/orders";
    }

    @PostMapping("/reject/{id}")
    public String rejectOrder(@PathVariable long id){
        Order orderToReject = orderDao.getOne(id);
        orderToReject.setStatus(OrderStatus.REJECTED);
        orderDao.save(orderToReject);
        return "redirect:/orders";
    }

   



}
