package com.example.bakingbuddy.demo.controllers;

import com.example.bakingbuddy.demo.Model.Image;
import com.example.bakingbuddy.demo.Model.User;
import com.example.bakingbuddy.demo.Repos.ReviewRepository;
import com.example.bakingbuddy.demo.Repos.ImageRepository;
import com.example.bakingbuddy.demo.Repos.OrderRepository;
import com.example.bakingbuddy.demo.Repos.UserRepository;
import com.example.bakingbuddy.demo.services.EmailService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UserController {
    private UserRepository usersDao;
    private ImageRepository imageDao;
    private PasswordEncoder passwordEncoder;
    private OrderRepository orderDao;
    private final EmailService emailService;
    private ReviewRepository reviewDao;

    public UserController(UserRepository usersDao, PasswordEncoder passwordEncoder, EmailService emailService, ImageRepository imageDao, OrderRepository orderDao, ReviewRepository reviewDao) {
        this.usersDao = usersDao;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.reviewDao = reviewDao;
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
        User userDb = usersDao.getOne(sessionUser.getId());
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
        User dbUser = usersDao.save(userToBeSaved);
        Image profileImage = new Image(true, uploadedImage, dbUser);
        imageDao.save(profileImage);
        emailService.userCreatedProfileEmail(dbUser, "Registration", "Congratulations on setting up your Baking Buddy profile!");
        return "redirect:/login";
    }

    @GetMapping("/users/{id}/edit")
    public String showEditUserForm(@PathVariable long id, Model model) {
        model.addAttribute("user", usersDao.getOne(id));
        model.addAttribute("profileImage", imageDao.getOne(id));
        return "users/user-profile";
    }

//    @PostMapping("/user/{}/edit")
//    public String editUser(@ModelAttribute User userToBeEdited) {
//
//    }

    @GetMapping("/")
    public String showHomePage(Model model){
        List users = userDao.findAll();
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
