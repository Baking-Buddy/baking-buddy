package com.example.bakingbuddy.demo.controllers;

import com.example.bakingbuddy.demo.Model.Review;
import com.example.bakingbuddy.demo.Model.User;
import com.example.bakingbuddy.demo.Repos.ReviewRepository;
import com.example.bakingbuddy.demo.Repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;

@Controller
public class ReviewController {
    @Autowired
    private ReviewRepository reviewDoa;

    @Autowired
    private UserRepository userDao;




    @GetMapping("/review")
    public String showReviews(Model model){
        User userDb = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", userDao.getOne(userDb.getId()));
        return "review/review";
    }

    @GetMapping("/review/create")
    public String showCreateReview(Model model){
        User userDb = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", userDao.getOne(userDb.getId()));
        model.addAttribute("review", new Review());
        return "review/create-review";
    }

    @PostMapping("/review/create")
    public String createReview(@ModelAttribute Review reviewToBeSaved){
        User userDb = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        reviewToBeSaved.setOwner(userDb);
        reviewToBeSaved.setDate(new Date());
        reviewDoa.save(reviewToBeSaved);
        return "redirect:/review";
    }

}
