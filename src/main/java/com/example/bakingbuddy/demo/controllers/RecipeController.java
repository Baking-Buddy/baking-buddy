package com.example.bakingbuddy.demo.controllers;


import com.example.bakingbuddy.demo.Model.Consumable;
import com.example.bakingbuddy.demo.Model.Recipe;
import com.example.bakingbuddy.demo.Model.User;
import com.example.bakingbuddy.demo.Repos.ConsumableRepository;
import com.example.bakingbuddy.demo.Repos.RecipeRepository;
import com.example.bakingbuddy.demo.Repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class RecipeController {
    @Autowired
    private RecipeRepository recipeDao;

    @Autowired
    private ConsumableRepository consumableDao;

    @Autowired
    private UserRepository userDao;

    @GetMapping("/recipe")
    public String showRecipes(Model model){
        User userDb = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", userDao.getOne(userDb.getId()));
        model.addAttribute("recipe", recipeDao.findAll());
        model.addAttribute("consumables", consumableDao.findAll());
        return "recipe/recipes";
    }

    @GetMapping("/recipe/create")
    public String showCreateRecipe(Model model){
        User userDb = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", userDao.getOne(userDb.getId()));
        model.addAttribute("consumable", consumableDao.findAll());
        model.addAttribute("recipe", new Recipe());
        return "recipe/create-recipe";
    }

    @PostMapping("/recipe/create")
    public String createdRecipe(@ModelAttribute Recipe recipeToBeSaved,
                                @RequestParam(name= "consumables")List<Consumable> consumables){
        User userDb = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        recipeToBeSaved.setOwner(userDb);
        recipeToBeSaved.setConsumables(consumables);
        recipeDao.save(recipeToBeSaved);
        return "redirect:/recipe";
    }
}
