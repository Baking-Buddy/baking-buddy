package com.example.bakingbuddy.demo.controllers;

import com.example.bakingbuddy.demo.Repos.UserRepository;
import com.example.bakingbuddy.demo.services.MailgunService;
import com.example.bakingbuddy.demo.services.UserService;
import kong.unirest.JsonNode;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PasswordResettingController {

    @Autowired
    private MailgunService mailgunService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userDao;

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "user/reset-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword() {


        return "user/forgot-password";
    }

}
