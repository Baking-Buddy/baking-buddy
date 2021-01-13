package com.example.bakingbuddy.demo.controllers;

import com.example.bakingbuddy.demo.Model.*;
import com.example.bakingbuddy.demo.Repos.ImageRepository;
import com.example.bakingbuddy.demo.Repos.OrderImageRepository;
import com.example.bakingbuddy.demo.Repos.OrderRepository;
import com.example.bakingbuddy.demo.Repos.UserRepository;
import com.example.bakingbuddy.demo.services.EmailService;
import com.example.bakingbuddy.demo.services.MailgunService;
import com.example.bakingbuddy.demo.services.ProductService;
import com.example.bakingbuddy.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class OrderController {

    @Autowired
    private OrderRepository orderDao;

    @Autowired
    private UserRepository userDao;

    @Autowired
    private ImageRepository imageDao;

    @Autowired
    private ProductService service;

    @Autowired
    private EmailService emailService;

    @Autowired
    private MailgunService mailgunService;

    @Autowired
    private UserService userService;


    @GetMapping("/orders/{id}")
    public String order(@PathVariable long id, Model viewModel){
        if (userService.isLoggedIn()) {
            User sessionUser = userService.sessionUser();
            if (userService.orderOwner(sessionUser, id) || userService.orderBaker(sessionUser, id)) {
                viewModel.addAttribute("order", orderDao.getOne(id));
                viewModel.addAttribute("user", sessionUser);
                viewModel.addAttribute("isBaker", sessionUser.isBaker());
                Image profileImage = imageDao.findByOwner(sessionUser);
                viewModel.addAttribute("profileImage", profileImage.getImageURL());
                return "orders/customer-order";
            } else {
                return "redirect:/orders";
            }
        } else {
            return "redirect:/login";
        }
    }


    @GetMapping("/orders/create/{id}")
    public String showOrderForm(Model viewModel, @PathVariable long id){
        if (userService.isLoggedIn()) {
            User sessionUser = userService.sessionUser();
            viewModel.addAttribute("user", sessionUser);
            viewModel.addAttribute("bakerID", id);
            viewModel.addAttribute("isBaker", sessionUser.isBaker());
            Image profileImage = imageDao.findByOwner(sessionUser);
            viewModel.addAttribute("profileImage", profileImage.getImageURL());
            return "orders/create";
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/orders/create/{id}")
    public String createOrder(@RequestParam(name="date") String date,
                              @RequestParam(name="description") String description,
                              @PathVariable long id) throws ParseException {
        Order orderToBeSaved = new Order();
        User sessionUser = userService.sessionUser();

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date convertedDate =df.parse(date);

        orderToBeSaved.setDate(convertedDate);
        orderToBeSaved.setDescription(description);
        orderToBeSaved.setBaker(userDao.getOne(id));
        orderToBeSaved.setOwner(sessionUser);
        orderToBeSaved.setOrderImage("");
        orderToBeSaved.setStatus(OrderStatus.PENDING);
        Order dbOrder = orderDao.save(orderToBeSaved);

        User emailReciever = userDao.getOne(id);
        String emailSubject = "Order Recieved from: " + sessionUser.getFirstName() + " " + sessionUser.getLastName();
        mailgunService.sendSimpleMessage(emailReciever, emailSubject, dbOrder.getDescription());
        return "redirect:/orders/" + dbOrder.getId();
    }

@GetMapping("/orders")
public String showOrders(@Param("query") String query, Model model) {
        if (userService.isLoggedIn()) {
            User sessionUser = userService.sessionUser();
            if (sessionUser.isBaker()) {
                model.addAttribute("orders", service.listAllBaker(query, sessionUser));
                model.addAttribute("user", sessionUser);
            } else if (!sessionUser.isBaker()) {
                model.addAttribute("orders", service.listAllOwner(query, sessionUser));
                model.addAttribute("user", sessionUser);
            }
            model.addAttribute("isBaker", sessionUser.isBaker());
            Image profileImage = imageDao.findByOwner(sessionUser);
            model.addAttribute("profileImage", profileImage.getImageURL());
            return "orders/orders";
        } else {
            return "redirect:/login";
        }
}


    @GetMapping("/orders/{id}/edit")
    public String editOrderForm(@PathVariable long id, Model model){
        if (userService.isLoggedIn()) {
            User sessionUser = userService.sessionUser();
            if(userService.orderOwner(sessionUser, id) || userService.orderBaker(sessionUser, id)) {
                model.addAttribute("user", sessionUser);
                model.addAttribute("order", orderDao.getOne(id));
                model.addAttribute("isBaker", sessionUser.isBaker());
                Image profileImage = imageDao.findByOwner(sessionUser);
                model.addAttribute("profileImage", profileImage.getImageURL());
                return "orders/edit-order";
            }else {
                return "redirect:/orders";
            }
        } else{
            return "redirect:/login";
        }
    }

    @PostMapping("/orders/{id}/edit")
    public String submitOrderEdit(
            @PathVariable long id,
            @RequestParam(name="description") String description,
            @RequestParam(name="date") String date,
            @RequestParam(name="price") double price,
            @RequestParam(name = "uploadedImage") String imageURL) throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date convertedDate =df.parse(date);
        Order orderToBeEdited = orderDao.getOne(id);
        orderToBeEdited.setDescription(description);
        orderToBeEdited.setDate(convertedDate);
        User sessionUser = userService.sessionUser();
        if (sessionUser.isBaker()){
            orderToBeEdited.setPrice(price);
            orderToBeEdited.setOrderImage(imageURL);
        }
        orderDao.save(orderToBeEdited);
        return "redirect:/orders";
    }

    @PostMapping("/accept/{id}")
    public String acceptOrder(@PathVariable long id){
        Order orderToAccept = orderDao.getOne(id);
        orderToAccept.setStatus(OrderStatus.ACCEPTED);
        orderDao.save(orderToAccept);

        User emailReciever = orderToAccept.getOwner();
        String emailSubject = "Order status has been changed";
        mailgunService.sendSimpleMessage(emailReciever, emailSubject, "Order has been " + orderToAccept.getStatus());
        return "redirect:/orders";
    }

    @PostMapping("/reject/{id}")
    public String rejectOrder(@PathVariable long id){
        Order orderToReject = orderDao.getOne(id);
        orderToReject.setStatus(OrderStatus.REJECTED);
        orderDao.save(orderToReject);

        User emailReciever = orderToReject.getOwner();
        String emailSubject = "Order status has been changed";
        mailgunService.sendSimpleMessage(emailReciever, emailSubject, "Order has been " + orderToReject.getStatus());
        return "redirect:/orders";
    }

}
