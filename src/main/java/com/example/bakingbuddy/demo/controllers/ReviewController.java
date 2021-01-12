package com.example.bakingbuddy.demo.controllers;

import com.example.bakingbuddy.demo.Model.Review;
import com.example.bakingbuddy.demo.Model.User;
import com.example.bakingbuddy.demo.Repos.ReviewRepository;
import com.example.bakingbuddy.demo.Repos.UserRepository;
import com.example.bakingbuddy.demo.services.UserService;
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

    @Autowired
    private UserService userService;


    @GetMapping("/review/{id}")
    public String showReview(@PathVariable long id, Model model){
        if(!userService.isLoggedIn()){
            return "redirect:/login";
        }
        User sessionUser = userService.sessionUser();
        model.addAttribute("user", sessionUser.getId());
        model.addAttribute("reviews", reviewDao.getOne(id));
        model.addAttribute("isBaker", sessionUser.isBaker());
        return "review/show-review";
    }

    @GetMapping("/review/{id}/create")
    public String showCreateReview(Model model, @PathVariable long id){
        if (userService.isLoggedIn()){
            model.addAttribute("review", new Review());
            model.addAttribute("bakerID", id);
            User sessionUser = userService.sessionUser();
            model.addAttribute("user", sessionUser.getId());
            model.addAttribute("isBaker", sessionUser.isBaker());
            return "review/create-review";
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/review/{id}/create")
    public String createReview(@ModelAttribute Review reviewToBeSaved,
                               @PathVariable long id){
        User sessionUser = userService.sessionUser();
        reviewToBeSaved.setOwner(sessionUser);
        reviewToBeSaved.setBaker(userDao.getOne(id));
        reviewToBeSaved.setDate(new Date());
        reviewDao.save(reviewToBeSaved);
        return "redirect:/reviews/{id}";
    }

    @GetMapping("/reviews/{id}")
    public String showBakersReviews(Model model, @PathVariable long id){
        User baker = userDao.getOne(id);
        if (userService.isLoggedIn()){
            User sessionUser = userService.sessionUser();
            model.addAttribute("user", sessionUser);
            model.addAttribute("isBaker", sessionUser.isBaker());
        }
        model.addAttribute("baker", baker);
        model.addAttribute("reviews", reviewDao.findAllByBaker(baker));
        return "review/review";
    }

    @GetMapping("/review/{id}/edit/{reviewID}")
    public String showEditForm(@PathVariable long reviewID, @PathVariable long id,  Model model){
    if (!userService.isLoggedIn()){
        return "redirect:/login";
    }
    User sessionUser = userService.sessionUser();
    if (!userService.reviewOwner(sessionUser, reviewID)){
        return  "redirect:/reviews/" + id;
    }
    model.addAttribute("reviewToEdit", reviewDao.getOne(reviewID));
    model.addAttribute("user", userDao.getOne(id));
    model.addAttribute("isBaker", sessionUser.isBaker());
    return "review/edit-review";
    }

    @PostMapping("/review/{id}/edit/{reviewID}")
    public String updateReview(@PathVariable long id,
                               @PathVariable long reviewID,
                               @RequestParam(name="title") String title,
                               @RequestParam(name="rating") int rating,
                               @RequestParam(name="body") String body){

            Review reviewToEdit = reviewDao.getOne(reviewID);
            reviewToEdit.setTitle(title);
            reviewToEdit.setRating(rating);
            reviewToEdit.setBody(body);
            reviewToEdit.setDate(new Date());
            reviewDao.save(reviewToEdit);
        return "redirect:/reviews/{id}";
        }

    @PostMapping("/review/{id}/delete/{reviewID}")
    public String deleteReview(@PathVariable long id, @PathVariable long reviewID){
        reviewDao.deleteById(reviewID);
        return"redirect:/reviews/{id}";
        }

}
