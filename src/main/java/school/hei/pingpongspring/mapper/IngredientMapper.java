package school.hei.pingpongspring.mapper;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import school.hei.pingpongspring.model.Ingredient;
import school.hei.pingpongspring.repository.dao.IngredientPriceDAO;
import school.hei.pingpongspring.repository.dao.StockMovementDAO;

import java.sql.ResultSet;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class IngredientMapper implements Function<ResultSet, Ingredient> {
    private final IngredientPriceDAO priceDAO;
    private final StockMovementDAO stockMovementDAO;

    @SneakyThrows
    public Ingredient apply(ResultSet resultSet){
        Ingredient ingredients = new Ingredient();
        return ingredients;
    }
}
