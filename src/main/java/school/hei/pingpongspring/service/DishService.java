package school.hei.pingpongspring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import school.hei.pingpongspring.model.Dish;
import school.hei.pingpongspring.model.Ingredient;
import school.hei.pingpongspring.model.StockMovement;
import school.hei.pingpongspring.repository.dao.DishDAO;
import school.hei.pingpongspring.repository.dao.IngredientDAO;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DishService {
    private final DishDAO subjectDish;
    private final IngredientDAO subjectIngredient;


    public Dish findById(long id) {
        return subjectDish.findById(id);
    }

    public List<Dish> getAll(int page, int size){
        return subjectDish.getAll(page, size);
    }

    public List<Ingredient> getIngredientByDish(long dishId){
        return subjectDish.findIngredientByDish(dishId);
    }

    public Dish addIngredients(Long dishId, List<Ingredient> ingredientsToAdd) {
        Dish dish = subjectDish.findById(dishId);

        List<Ingredient> savedIngredients = subjectIngredient.saveAll(ingredientsToAdd);

        dish.addIngredients(ingredientsToAdd);
        return dish;
    }
}
