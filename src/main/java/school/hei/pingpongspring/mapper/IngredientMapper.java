package school.hei.pingpongspring.mapper;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import school.hei.pingpongspring.model.Ingredient;
import school.hei.pingpongspring.model.IngredientPrice;
import school.hei.pingpongspring.model.StockMovement;
import school.hei.pingpongspring.model.Unit;
import school.hei.pingpongspring.repository.dao.IngredientPriceDAO;
import school.hei.pingpongspring.repository.dao.StockMovementDAO;

import java.sql.ResultSet;
import java.util.List;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class IngredientMapper implements Function<ResultSet, Ingredient> {
    private final IngredientPriceDAO priceDAO;
    private final StockMovementDAO stockMovementDAO;

    @Override
    @SneakyThrows
    public Ingredient apply(ResultSet res){
        long ingredientId = res.getLong("id");
        List<IngredientPrice> prices = priceDAO.findByIdIngredient(ingredientId);
        List<StockMovement> stockMovements = stockMovementDAO.findByIngredient(ingredientId);

        Ingredient ingredient = new Ingredient();
        ingredient.setId(res.getInt("id"));
        ingredient.setName(res.getString("name"));
        ingredient.setDateTime(res.getTimestamp("dateTime").toInstant());
        ingredient.setPrices(prices);
        ingredient.setUnit(Unit.valueOf(res.getString("unit")));
        ingredient.setStockMovements(stockMovements);
        return ingredient;
    }
}
