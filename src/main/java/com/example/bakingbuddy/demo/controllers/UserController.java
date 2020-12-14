package com.example.bakingbuddy.demo.controllers;

import com.example.bakingbuddy.demo.Model.User;
import com.example.bakingbuddy.demo.Repos.UserRepository;
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

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return"users/register";
    }

    @PostMapping("/register")
    public String saveUser(@ModelAttribute User user) {
        usersDao.save(user);
        return "redirect:/login";
    }

}
