package school.hei.pingpongspring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import school.hei.pingpongspring.model.Dish;
import school.hei.pingpongspring.model.Ingredient;
import school.hei.pingpongspring.repository.dao.DishDAO;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DishService {
    public final DishDAO subjectDish;

    public Dish findById(long id) {
        return subjectDish.findById(id);
    }

    public List<Ingredient> getIngredientByDish(long dishId){
        return subjectDish.findIngredientByDish(dishId);
    }
}
