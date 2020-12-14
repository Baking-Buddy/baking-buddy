package com.example.bakingbuddy.demo.controllers;

import com.example.bakingbuddy.demo.Model.User;
import com.example.bakingbuddy.demo.Repos.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {
    private UserRepository usersDao;

    public UserController(UserRepository usersDao) {
        this.usersDao = usersDao;
    }

    @GetMapping("/sign-up")
    public String showUsers(Model model) {
        model.addAttribute("users", usersDao.findAll());
        return"users";
    }

    @GetMapping("/dashboard")
    public String showDashboard() {
        return "users/dashboard";
    }



//    @PostMapping("/sign-up")
//    public String saveUser(@ModelAttribute User user) {
//        String hash = passwordEncoder.encoder(user.getPassword());
//        user.setPassword(hash);
//        usersDao.save(user);
//        return "redirect:/login";
//    }

}
