package com.example.bakingbuddy.demo.controllers;

import com.example.bakingbuddy.demo.Model.Review;
import com.example.bakingbuddy.demo.Model.User;
import com.example.bakingbuddy.demo.Repos.ReviewRepository;
import com.example.bakingbuddy.demo.Repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
public class ReviewController {
    @Autowired
    private ReviewRepository reviewDao;

    @Autowired
    private UserRepository userDao;




    @GetMapping("/review/{id}")
    public String showReview(@PathVariable long id, Model model){

        model.addAttribute("reviews", reviewDao.getOne(id));
        return "review/review";
    }

    @GetMapping("/review/create/{id}")
    public String showCreateReview(Model model,
                                   @PathVariable long id){
        model.addAttribute("review", new Review());
        model.addAttribute("bakerID", id);
        return "review/create-review";
    }

    @PostMapping("/review/create/{id}")
    public String createReview(@ModelAttribute Review reviewToBeSaved,
                               @PathVariable long id){
        User sessionUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userDb = userDao.getOne(sessionUser.getId());

        reviewToBeSaved.setOwner(userDb);
        reviewToBeSaved.setBaker(userDao.getOne(id));
        reviewToBeSaved.setDate(new Date());
        reviewDao.save(reviewToBeSaved);
        return "redirect:/reviews/{id}";
    }

    @GetMapping("/reviews/{id}")
    public String showBakersReviews(Model model, @PathVariable long id){
        User baker = userDao.getOne(id);
        model.addAttribute("user", baker);
        model.addAttribute("reviews", reviewDao.findAllByBaker(baker));
        return "review/review";
    }

}
