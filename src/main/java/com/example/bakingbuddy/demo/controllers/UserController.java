package com.example.bakingbuddy.demo.controllers;

import com.example.bakingbuddy.demo.Model.Image;
import com.example.bakingbuddy.demo.Model.User;
import com.example.bakingbuddy.demo.Repos.ReviewRepository;
import com.example.bakingbuddy.demo.Repos.ImageRepository;
import com.example.bakingbuddy.demo.Repos.OrderRepository;
import com.example.bakingbuddy.demo.Repos.UserRepository;
import com.example.bakingbuddy.demo.services.EmailService;
import com.example.bakingbuddy.demo.services.MailgunService;
import com.example.bakingbuddy.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
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
    private final UserService userService;
    private ReviewRepository reviewDao;
    private MailgunService mailgunService;

    @Value("${filestackApiKey}")
    private String fileStackApiKey;


    public UserController(UserRepository usersDao, PasswordEncoder passwordEncoder, EmailService emailService, ImageRepository imageDao, OrderRepository orderDao, ReviewRepository reviewDao, UserService userService, MailgunService mailgunService) {
        this.usersDao = usersDao;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.userService = userService;
        this.reviewDao = reviewDao;
        this.imageDao = imageDao;
        this.orderDao = orderDao;
        this.mailgunService = mailgunService;
    }


    @RequestMapping(path = "/keys.js", produces = "application/javascript")
    @ResponseBody
    public String apikey(){
        System.out.println(fileStackApiKey);
        return "const filestackAPIKey = `" + fileStackApiKey + "`";
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
        if (userService.isLoggedIn()) {
            User sessionUser = userService.sessionUser();
            model.addAttribute("pendingOrders", orderDao.findAll());
            model.addAttribute("user", sessionUser);
            model.addAttribute("isBaker", sessionUser.isBaker());
            model.addAttribute("profileImage",userService.profileImage(sessionUser));
            return "users/dashboard";
        } else {
            return "redirect:/login";
        }
    }


    @PostMapping("/register")
    public String saveUser(@Valid User userToBeSaved,
                           BindingResult bindingResult,
                           RedirectAttributes ra,
                           Errors validation,
                           @RequestParam(required = false) boolean isBaker,
                           @RequestParam(name="uploadedImage") String uploadedImage,
                           @RequestParam(name="rpassword") String rpassword,
                           Model model
    ) {

        if(userService.userExists(userToBeSaved.getEmail())) {
            bindingResult.addError(new FieldError("user", "email", "Email address already in use"));
        }
        if(userService.usernameExists(userToBeSaved.getUsername())) {
            bindingResult.addError(new FieldError("user", "username", "Username already in use"));
        }
        if(userToBeSaved.getPassword() != null && rpassword != null) {
            if(!userToBeSaved.getPassword().equals(rpassword)) {
//                bindingResult.addError(new FieldError("user", "rpassword", "Passwords must match"));
                validation.reject("rpassword");
            }
        }
        if(bindingResult.hasErrors()){
            return "users/register";
        }

        ra.addFlashAttribute("message", "Account has been created");
        userToBeSaved.setBaker(isBaker);
        String hashPassword = passwordEncoder.encode(userToBeSaved.getPassword());
        userToBeSaved.setPassword(hashPassword);
        User dbUser = usersDao.save(userToBeSaved);
        Image profileImage = new Image(true, uploadedImage, dbUser);
        imageDao.save(profileImage);
        mailgunService.sendRegisterMessage(userToBeSaved, false);
        return "redirect:/login";
    }

    @GetMapping("/error")
    public String error(){
        return "/error";
    }

    @GetMapping("/user/{id}/edit")
    public String showEditUserForm(@PathVariable long id, Model model) {
        if (!userService.isLoggedIn()){
            return "redirect:/login";
        }
        User sessionUser = userService.sessionUser();
        if (!userService.profileOwner(sessionUser, id)){
            return "redirect:/user/" + sessionUser.getId() + "/edit";
        }
        model.addAttribute("user", usersDao.getOne(id));
        model.addAttribute("isBaker", sessionUser.isBaker());
        model.addAttribute("profileImage",userService.profileImage(sessionUser));
        return "users/edit-profile";
    }

    @PostMapping("/user/{id}/edit")
    public String editUser(
            @PathVariable long id,
            @RequestParam(name="firstName") String firstName,
            @RequestParam(name="lastName") String lastName,
            @RequestParam(name="city") String city,
            @RequestParam(name="state") String state,
            @RequestParam(name="email") String email) {
        User sessionuser = userService.sessionUser();
        Image imageToBeEdited = imageDao.findByOwner(sessionuser);
        sessionuser.setFirstName(firstName);
        sessionuser.setLastName(lastName);
        sessionuser.setCity(city);
        sessionuser.setState(state);
        sessionuser.setEmail(email);
//        imageToBeEdited.setImageURL(profilePicture);
//        imageDao.save(imageToBeEdited);
        usersDao.save(sessionuser);
        mailgunService.sendSimpleMessage(sessionuser, "SETTINGS UPDATED", "Some changes have been made to your account.");
        return "redirect:/dashboard";
    }

    @GetMapping("/")
    public String showHomePage(Model model){
        if (userService.isLoggedIn()){
           User sessionUser = userService.sessionUser();
            model.addAttribute("user", sessionUser);
            model.addAttribute("isBaker", sessionUser.isBaker());
            model.addAttribute("profileImage",userService.profileImage(sessionUser));
        }
        List<User> users = usersDao.findAll();
        model.addAttribute("users", users);
        return "home/index";
    }


    @GetMapping("/baker-profile/{id}")
    public String showBakerProfile(@PathVariable long id, Model model){
        User user = usersDao.getOne(id);
        if (userService.isLoggedIn()){
            User sessionUser = userService.sessionUser();
            Image profileImage = imageDao.findByOwner(sessionUser);
            model.addAttribute("profileImage", profileImage.getImageURL());
            boolean sendMessage = false;
            if (!userService.profileOwner(sessionUser, id)){
                sendMessage = true;
                model.addAttribute("sendMessage", sendMessage);
            }
            model.addAttribute("isBaker", sessionUser.isBaker());
        }
        model.addAttribute("isAnonymous", userService.isLoggedIn());
        model.addAttribute("user", user);
        return "users/baker-profile";

    }

    @GetMapping("/developers")
        public String showDevelopers(){
        return "AboutUs/developers";
        }




}
