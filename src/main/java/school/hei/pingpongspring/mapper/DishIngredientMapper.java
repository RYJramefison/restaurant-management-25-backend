package school.hei.pingpongspring.mapper;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import school.hei.pingpongspring.model.*;
import school.hei.pingpongspring.repository.dao.IngredientPriceDAO;
import school.hei.pingpongspring.repository.dao.StockMovementDAO;

import java.sql.ResultSet;
import java.util.List;
import java.util.function.Function;

@Component
public class DishIngredientMapper implements Function<ResultSet, DishIngredient> {

    @Override
    @SneakyThrows
    public DishIngredient apply(ResultSet res){
        DishIngredient dishIngredient = new DishIngredient();
        dishIngredient.setDishId(res.getLong("dish_id"));
        dishIngredient.setIngredientId(res.getLong("ingredient_id"));
        dishIngredient.setRequiredQuantity(res.getFloat("required_quantity"));
        dishIngredient.setUnit(Unit.valueOf(res.getString("unit")));;
        return dishIngredient;
    }
}
