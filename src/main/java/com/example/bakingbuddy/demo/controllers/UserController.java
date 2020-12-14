package com.example.bakingbuddy.demo.controllers;

import com.example.bakingbuddy.demo.Model.User;
import com.example.bakingbuddy.demo.Repos.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {
    private UserRepository usersDao;

    public UserController(UserRepository usersDao) {

        this.usersDao = usersDao;
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        boolean is_baker = false;
        model.addAttribute("is_baker", is_baker);
        model.addAttribute("user", new User());
        return"users/register";
    }

    @PostMapping("/register")
    public String saveUser(@ModelAttribute User user,
                           @RequestParam(required = false) boolean isBaker) {
        user.setBaker(isBaker);
        usersDao.save(user);
        return "redirect:/login";
    }

}
