package school.hei.pingpongspring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import school.hei.pingpongspring.controller.rest.CreateOrUpdateDishIngredient;
import school.hei.pingpongspring.model.Dish;
import school.hei.pingpongspring.model.DishIngredient;
import school.hei.pingpongspring.model.Ingredient;
import school.hei.pingpongspring.model.StockMovement;
import school.hei.pingpongspring.repository.dao.DishDAO;
import school.hei.pingpongspring.repository.dao.Dish_IngredientDAO;
import school.hei.pingpongspring.repository.dao.IngredientDAO;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DishService {
    private final DishDAO subjectDish;
    private final IngredientDAO subjectIngredient;
    private final Dish_IngredientDAO subjectDishIngredient;


    public Dish findById(long id) {
        return subjectDish.findById(id);
    }

    public List<Dish> getAll(int page, int size){
        return subjectDish.getAll(page, size);
    }

    public List<Ingredient> getIngredientByDish(long dishId){
        return subjectDish.findIngredientByDish(dishId);
    }

    public Dish addIngredients(Long dishId, List<CreateOrUpdateDishIngredient> ingredientsToAdd) {
        Dish dish = subjectDish.findById(dishId);

        List<Ingredient> ingredient = new ArrayList<>();
        for (CreateOrUpdateDishIngredient c : ingredientsToAdd) {
            Ingredient ingredient1 = new Ingredient(c.getId(),c.getName(),c.getUnit());
            ingredient.add(ingredient1);

        }
        List<Ingredient> savedIngredients = subjectIngredient.saveAll(ingredient);
        for (CreateOrUpdateDishIngredient c : ingredientsToAdd) {
            DishIngredient dishIngredient = new DishIngredient(dishId,c.getId(), c.getRequiredQuantity(),c.getUnit());
            subjectDishIngredient.save(dishIngredient);
        }
        dish.addIngredients(savedIngredients);
        return dish;
    }
}
