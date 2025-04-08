package school.hei.pingpongspring.mapper;


import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import school.hei.pingpongspring.model.MovementType;
import school.hei.pingpongspring.model.StockMovement;
import school.hei.pingpongspring.model.Unit;

import java.sql.ResultSet;
import java.util.function.Function;

@Component
public class StockMovementMapper implements Function<ResultSet, StockMovement> {
    @SneakyThrows
    @Override
    public StockMovement apply(ResultSet resultSet) {
        StockMovement stockMovement = new StockMovement();
        stockMovement.setId(resultSet.getLong("id"));
        stockMovement.setIngredientId(resultSet.getLong("ingredient_id"));
        stockMovement.setType(MovementType.valueOf(resultSet.getString("type")));
        stockMovement.setQuantity(resultSet.getDouble("quantity"));
        stockMovement.setUnit(Unit.valueOf(resultSet.getString("unit")));
        stockMovement.setDate(resultSet.getTimestamp("date").toInstant());
        return stockMovement;
    }
}
