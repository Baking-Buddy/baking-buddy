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
import org.springframework.web.bind.annotation.*;

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
                                @RequestParam("consumables")List<Consumable> consumables){
        User userDb = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        recipeToBeSaved.setOwner(userDb);
        recipeToBeSaved.setConsumables(consumables);
        recipeDao.save(recipeToBeSaved);
        return "redirect:/recipe";
    }

    @GetMapping("/recipe/{id}/edit")
    public String showEditForm(@PathVariable long id, Model model){
        User userDb = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", userDao.getOne(userDb.getId()));
        model.addAttribute("consumable", consumableDao.findAll());
        model.addAttribute("recipe", new Recipe());
        model.addAttribute("recipeToEdit", recipeDao.getOne(id));
        return "recipe/edit-recipe";
    }

    @PostMapping("/recipe/{id}/edit")
    public String editRecipe(
            @PathVariable long id,
            @ModelAttribute Recipe recipeToEdit,
            @RequestParam("consumables")List<Consumable> consumables
    ){
        User userDb = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        recipeToEdit.setOwner(userDb);
        recipeToEdit.setConsumables(consumables);
        recipeDao.save(recipeToEdit);
        return "redirect:/recipe";
    }

    @PostMapping("/recipe/{id}/delete")
    public String deleteRecipe(@PathVariable long id
                               ){
        Recipe recipe = recipeDao.getOne(id);
        recipe.setConsumables(null);
        recipeDao.save(recipe);
        recipeDao.deleteById(id);
        return "redirect:/recipe";
    }
}
