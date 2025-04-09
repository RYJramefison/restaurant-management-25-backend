package school.hei.pingpongspring.controller.mapper;

import org.springframework.stereotype.Component;
import school.hei.pingpongspring.controller.rest.PriceRest;
import school.hei.pingpongspring.model.IngredientPrice;

import java.util.function.Function;

@Component
public class PriceRestMapper implements Function<IngredientPrice, PriceRest> {

    @Override
    public PriceRest apply(IngredientPrice price) {
        return new PriceRest(price.getId(),price.getIngredientId(), price.getPrice(), price.getDate());
    }
}