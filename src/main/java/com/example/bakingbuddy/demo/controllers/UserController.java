package com.example.bakingbuddy.demo.controllers;

import com.example.bakingbuddy.demo.Model.Image;
import com.example.bakingbuddy.demo.Model.User;
import com.example.bakingbuddy.demo.Repos.ReviewRepository;
import com.example.bakingbuddy.demo.Repos.ImageRepository;
import com.example.bakingbuddy.demo.Repos.OrderRepository;
import com.example.bakingbuddy.demo.Repos.UserRepository;
import com.example.bakingbuddy.demo.services.EmailService;
import com.example.bakingbuddy.demo.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
public class UserController {
    private UserRepository usersDao;
    private ImageRepository imageDao;
    private PasswordEncoder passwordEncoder;
    private OrderRepository orderDao;
    private final EmailService emailService;
    private ReviewRepository reviewDao;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserRepository usersDao, PasswordEncoder passwordEncoder, EmailService emailService, ImageRepository imageDao, OrderRepository orderDao, ReviewRepository reviewDao, UserService userService) {
        this.usersDao = usersDao;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.reviewDao = reviewDao;
        this.imageDao = imageDao;
        this.orderDao = orderDao;
        this.userService = userService;
    }

    @InitBinder
    public void intiBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class,stringTrimmerEditor);
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
    public String saveUser(@Valid User userToBeSaved,
                           BindingResult bindingResult,
                           RedirectAttributes ra,
                           @RequestParam(required = false) boolean isBaker,
                           @RequestParam(name="uploadedImage") String uploadedImage
    ) {

        if(userService.userExists(userToBeSaved.getEmail())) {
            bindingResult.addError(new FieldError("user", "email", "Email address already in use"));
        }
        if(userToBeSaved.getPassword() != null && userToBeSaved.getRpassword() != null) {
            if(!userToBeSaved.getPassword().equals(userToBeSaved.getRpassword())) {
                bindingResult.addError(new FieldError("user", "rpassword", "Passwords must match"));
            }
        }
        if(bindingResult.hasErrors()){
            return "users/register";
        }

        ra.addFlashAttribute("message", "Account has been created");
        userToBeSaved.setBaker(isBaker);
        String hashPassword = passwordEncoder.encode(userToBeSaved.getPassword());
        userToBeSaved.setPassword(hashPassword);
        String hashRPassword = passwordEncoder.encode(userToBeSaved.getRpassword());
        userToBeSaved.setRpassword(hashRPassword);
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
