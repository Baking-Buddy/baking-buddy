package com.example.bakingbuddy.demo.controllers;

import com.example.bakingbuddy.demo.Model.User;
import com.example.bakingbuddy.demo.Repos.ReviewRepository;
import com.example.bakingbuddy.demo.Repos.UserRepository;
import com.example.bakingbuddy.demo.services.EmailService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UserController {
    private UserRepository usersDao;
    private PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private ReviewRepository reviewDao;

    public UserController(UserRepository usersDao, PasswordEncoder passwordEncoder, EmailService emailService, ReviewRepository reviewDao) {

        this.usersDao = usersDao;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.reviewDao = reviewDao;
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
        return "users/dashboard";
    }


    @PostMapping("/register")
    public String saveUser(@ModelAttribute User user,
                           @RequestParam(required = false) boolean isBaker) {
        user.setBaker(isBaker);
        String hashPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashPassword);
        User dbUser = usersDao.save(user);
        emailService.userCreatedProfileEmail(dbUser, "Registration", "Congratulations on setting up your Baking Buddy profile!");
        return "redirect:/login";
    }


    @GetMapping("/")
    public String showHomePage(Model model){
        List users = usersDao.findAll();
        model.addAttribute("users", users);
//        model.addAttribute("reviews", reviewDao.findAllByBaker());
        return "home/index";
    }


    @GetMapping("/baker-profile/{id}")
    public String showBakerProfile(@PathVariable long id, Model model){
        User user = usersDao.getOne(id);
        model.addAttribute("user", user);
        return "users/baker-profile";
    }




}
