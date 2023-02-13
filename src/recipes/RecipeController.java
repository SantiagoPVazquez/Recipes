package recipes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import recipes.model.Recipe;
import recipes.model.RecipeService;
import recipes.model.User;
import recipes.model.UserService;

import javax.validation.Valid;
import java.util.List;


@RestController
@Validated
public class RecipeController {
    @Autowired
    private final RecipeService recipes;

    @Autowired
    private UserService userService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipes = recipeService;
    }

    @GetMapping("/api/recipe/{id}")
    public Recipe getRecipe(@PathVariable long id) {
        if (recipes.getRecipeById(id) != null) {
            return recipes.getRecipeById(id);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/api/recipe/new")
    public String postRecipe(@Valid @RequestBody Recipe inputRecipe, Authentication auth) {
        User user = userService.findUserByUsername(auth.getName());
        Recipe newRecipe = recipes.saveRecipe(inputRecipe);
        newRecipe.setUser(user);
        return "{ id: " + recipes.saveRecipe(newRecipe).getId() + "}";
    }

    @DeleteMapping("/api/recipe/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable long id, Authentication auth) {
        if (auth.getName().equals(recipes.getRecipeById(id).getUser().getEmail())) {
            return recipes.deleteRecipe(id);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        }
    @PutMapping("/api/recipe/{id}")
    public void updateRecipe (@PathVariable long id, @RequestBody @Valid Recipe newRecipe, Authentication auth) {
        if (recipes.getRecipeById(id) != null && auth.getName().equals(recipes.getRecipeById(id).getUser().getEmail())) {
            Recipe recipe = recipes.getRecipeById(id);
            recipe.setName(newRecipe.getName());
            recipe.setCategory(newRecipe.getCategory());
            recipe.setDirections(newRecipe.getDirections());
            recipe.setIngredients(newRecipe.getIngredients());
            recipe.setDescription(newRecipe.getDescription());
            recipes.saveRecipe(recipe);
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        } throw new ResponseStatusException(HttpStatus.FORBIDDEN);

    }
    @GetMapping("/api/recipe/search")
    public List<Recipe> searchRecipe(@Valid @RequestParam(name = "name", required = false) String name, @Valid @RequestParam(name = "category", required = false) String category) {
        if ((name == null && category == null) || (name != null && category != null)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } else {
            if (name != null) {
                return recipes.findRecipeByName(name);
            } else {
                return recipes.findRecipeByCategory(category);
            }
        }
    }
}



