package com.example.bakingbuddy.demo.controllers;

import com.example.bakingbuddy.demo.Model.User;
import com.example.bakingbuddy.demo.Repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SearchController {
    @Autowired
    private UserRepository usersDao;

//    @GetMapping("/search-results")
//    public String getSearchResults(){
//        return "home/search-results";
//    }

    @GetMapping("/search-results")
    public String bakerSearchResults(@RequestParam(name = "query") String query, Model model){
        List<User> userResults = usersDao.findBakerByUsernameLike(query);
        model.addAttribute("userResults", userResults);
        return "home/search-results";
    }
}
