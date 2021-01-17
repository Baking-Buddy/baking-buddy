package com.example.bakingbuddy.demo.controllers;

import com.example.bakingbuddy.demo.Model.Image;
import com.example.bakingbuddy.demo.Model.Review;
import com.example.bakingbuddy.demo.Model.User;
import com.example.bakingbuddy.demo.Repos.ImageRepository;
import com.example.bakingbuddy.demo.Repos.ReviewRepository;
import com.example.bakingbuddy.demo.Repos.UserRepository;
import com.example.bakingbuddy.demo.services.DateService;
import com.example.bakingbuddy.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
public class SearchController {
    @Autowired
    private UserRepository usersDao;
    @Autowired
    private UserService userService;
    @Autowired
    private ImageRepository imageDao;
    @Autowired
    private ReviewRepository reviewDao;
    @Autowired
    private DateService dateService;

    @GetMapping("/search-results")
    public String bakerSearchResults(@RequestParam(name = "query") String query, Model model){
        if (userService.isLoggedIn()){
            User sessionUser = userService.sessionUser();
            model.addAttribute("user", sessionUser);
            model.addAttribute("isBaker", sessionUser.isBaker());
            model.addAttribute("profileImage",userService.profileImage(sessionUser));
        }
        List<User> bakerResults = usersDao.findBakerByUsernameLike(query);
        HashMap<Long, String> bakerImages = new HashMap<>();
        for (User baker : bakerResults){
            bakerImages.put(baker.getId(), userService.profileImage(baker));
        }
        HashMap<Long, String> reviewDates = new HashMap<>();
        HashMap<Long, List<Review>> bakerReviews = new HashMap<>();
        for (User baker : bakerResults){
            List<Review> singleBakerReviews = reviewDao.findAllByBaker(baker);
            bakerReviews.put(baker.getId(), singleBakerReviews);
            for (Review review: singleBakerReviews){
                reviewDates.put(review.getId(), dateService.displayDate(review.getDate()));
            }
        }
        model.addAttribute("reviewDates", reviewDates);
        model.addAttribute("bakerImages", bakerImages);
        model.addAttribute("bakerReviews", bakerReviews);
        model.addAttribute("bakerResults", bakerResults);
        return "home/search-results";
    }
}
