package com.example.bakingbuddy.demo.controllers;

import com.example.bakingbuddy.demo.Model.User;
import com.example.bakingbuddy.demo.Repos.UserRepository;
import com.example.bakingbuddy.demo.Utility.Utility;
import com.example.bakingbuddy.demo.services.MailgunService;
import com.example.bakingbuddy.demo.services.UserService;
import kong.unirest.JsonNode;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
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
    public String processForgotPassword(HttpServletRequest request, Model model) {
        String email = request.getParameter("email");
        String token = RandomString.make(30);

        try {
            userService.updateResetPasswordToken(token, email);
            String resetPasswordLink = Utility.getSiteURL(request) + "/reset-password?token=" + token;

            JsonNode response = mailgunService.sendPasswordResetMessage(userDao.findUserByEmail(email), resetPasswordLink, false);
            System.out.println("response.toPrettyString() = " + response.toPrettyString());
            model.addAttribute("message", "We have sent a reset password link to your email. Please check.");

        } catch (UsernameNotFoundException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("message", "We have sent a reset password link to your email. Please check.");
        }
        return "users/forgot-password";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@Param(value = "token") String token, Model model) {
        User user = userService.getByResetPasswordToken(token);

        if (user == null) {
            model.addAttribute("message", "Invalid Token");
            return "users/reset-password";
        }
        else {
            model.addAttribute("token", token);
            return "users/reset-password";
        }
    }

    @PostMapping("/reset-password")
    public String processResetPassword(HttpServletRequest request, Model model) {
        String token = request.getParameter("token");
        String password = request.getParameter("password");

        User user = userService.getByResetPasswordToken(token);
        model.addAttribute("title", "Reset your password");

        if (user == null) {
            model.addAttribute("message", "Invalid Token");
            return "message";
        } else {
            userService.updatePassword(user, password);

            model.addAttribute("message", "You have successfully changed your password.");
        }

        return "users/reset-confirm";
    }

}
