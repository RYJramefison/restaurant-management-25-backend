package school.hei.pingpongspring.controller.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import school.hei.pingpongspring.controller.rest.*;
import school.hei.pingpongspring.model.Dish;
import school.hei.pingpongspring.model.Ingredient;
import school.hei.pingpongspring.repository.dao.IngredientDAO;

import java.util.List;

@Component
public class DishRestMapper {
    @Autowired
    private PriceRestMapper priceRestMapper;
    @Autowired private StockMovementRestMapper stockMovementRestMapper;
    @Autowired private IngredientRestMapper ingredientRestMapper;

    public Dish toRest(Dish dish) {
        List<Ingredient> ingredientRests = dish.getIngredients().stream()
                .map(ingredient -> ingredientRestMapper.apply(ingredient)).toList();
        return new Dish(dish.getId(), dish.getName(), dish.getPrice(),  ingredientRests);
    }


}
