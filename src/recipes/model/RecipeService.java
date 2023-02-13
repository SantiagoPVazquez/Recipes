package recipes.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import recipes.model.Recipe;
import recipes.model.RecipeRepository;

import javax.transaction.Transactional;
import java.util.List;


@Service
@Transactional
public class RecipeService {
    @Autowired
    private final RecipeRepository RECIPE_REPOSITORY;
    @Autowired
    public RecipeService(RecipeRepository recipeRepository) {
        this.RECIPE_REPOSITORY = recipeRepository;
    }


    public Recipe getRecipeById(long id) {
        if (RECIPE_REPOSITORY.existsById(id)) {
            return RECIPE_REPOSITORY.findById(id).get();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    public Recipe saveRecipe(Recipe recipe) {
        return RECIPE_REPOSITORY.save(recipe);
    }

    public List<Recipe> findRecipeByName(String name) {
        return RECIPE_REPOSITORY.findByNameIgnoreCaseContainsOrderByDateDesc(name);
    }

    public List<Recipe> findRecipeByCategory(String category) {
        return RECIPE_REPOSITORY.findByCategoryIgnoreCaseOrderByDateDesc(category);
    }
    public ResponseEntity<Void> deleteRecipe(Long id) {
        if (RECIPE_REPOSITORY.existsById(id)) {
            Recipe recipe = RECIPE_REPOSITORY.findById(id).get();
            RECIPE_REPOSITORY.delete(recipe);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }



}
