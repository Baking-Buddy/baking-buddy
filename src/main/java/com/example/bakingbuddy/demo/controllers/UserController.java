package com.example.bakingbuddy.demo.controllers;

import com.example.bakingbuddy.demo.Model.Image;
import com.example.bakingbuddy.demo.Model.User;
import com.example.bakingbuddy.demo.Repos.ImageRepository;
import com.example.bakingbuddy.demo.Repos.OrderRepository;
import com.example.bakingbuddy.demo.Repos.UserRepository;
import com.example.bakingbuddy.demo.services.EmailService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {
    private UserRepository userDao;
    private ImageRepository imageDao;
    private PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private OrderRepository orderDao;

    public UserController(UserRepository userDao, PasswordEncoder passwordEncoder, EmailService emailService, ImageRepository imageDao, OrderRepository orderDao) {

        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.imageDao = imageDao;
        this.orderDao = orderDao;
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        boolean is_baker = false;
        model.addAttribute("is_baker", is_baker);
        model.addAttribute("user", new User());
        return"users/register";
    }


    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        User sessionUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userDb = userDao.getOne(sessionUser.getId());
        model.addAttribute("pendingOrders", orderDao.findAll());
        model.addAttribute("user", userDb);
        return "users/dashboard";
    }


    @PostMapping("/register")
    public String saveUser(@ModelAttribute User userToBeSaved,
                           @RequestParam(required = false) boolean isBaker,
                           @RequestParam(name="uploadedImage") String uploadedImage
    ) {
        userToBeSaved.setBaker(isBaker);
        String hashPassword = passwordEncoder.encode(userToBeSaved.getPassword());
        userToBeSaved.setPassword(hashPassword);
        User dbUser = userDao.save(userToBeSaved);
        Image profileImage = new Image(true, uploadedImage, dbUser);
        imageDao.save(profileImage);
        emailService.userCreatedProfileEmail(dbUser, "Registration", "Congratulations on setting up your Baking Buddy profile!");
        return "redirect:/login";
    }


    @GetMapping("/")
    public String showHomePage(Model model){
        model.addAttribute("users", userDao.findAll());
        return "home/index";
    }


    @GetMapping("/baker-profile/{id}")
    public String showBakerProfile(@PathVariable long id, Model model){
        User user = userDao.getOne(id);
        model.addAttribute("user", user);
        return "users/baker-profile";
    }




}
