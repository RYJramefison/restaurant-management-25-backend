package school.hei.pingpongspring.mapper;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import school.hei.pingpongspring.model.IngredientPrice;

import java.sql.ResultSet;
import java.util.function.Function;

@Component
public class PriceMapper implements Function<ResultSet, IngredientPrice> {
    @SneakyThrows
    @Override
    public IngredientPrice apply(ResultSet resultSet) {
        IngredientPrice price = new IngredientPrice();
        price.setId(resultSet.getLong("id"));
        price.setPrice(resultSet.getDouble("price"));
        price.setDate(resultSet.getDate("date").toInstant());
        return price;
    }
}
