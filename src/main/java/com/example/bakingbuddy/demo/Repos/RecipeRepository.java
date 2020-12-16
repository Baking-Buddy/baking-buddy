package com.example.bakingbuddy.demo.Repos;

import com.example.bakingbuddy.demo.Model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    Recipe findRecipeByName(String name);
    Recipe findRecipeById(long id);
}
