package com.example.bakingbuddy.demo.controllers;

import com.example.bakingbuddy.demo.Model.User;
import com.example.bakingbuddy.demo.Repos.UserRepository;
import com.example.bakingbuddy.demo.services.MailgunService;
import com.example.bakingbuddy.demo.services.UserService;
import kong.unirest.JsonNode;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
        return "users/forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam(name="email") String email, String newPassword) {
        User user = userDao.findUserByEmail(email);
        String resetPasswordLink = "/reset-password/" + user.getId();
//        JsonNode response = mailgunService.sendPasswordResetMessage(userDao.findUserByEmail(email), resetPasswordLink, true);

        return "redirect:/reset-password/" + user.getId();
    }

    @GetMapping("/reset-password/{id}")
    public String resetPasswordForm(@PathVariable long id){
        User user = userDao.getOne(id);
        return "users/reset-password/" + user.getId();
    }

    @PostMapping("/reset-password/{id}")
    public String resetPassword(@PathVariable long id, String newPassword){
        User user = userDao.getOne(id);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        userDao.save(user);

        return "redirect:/login";
    }

}
