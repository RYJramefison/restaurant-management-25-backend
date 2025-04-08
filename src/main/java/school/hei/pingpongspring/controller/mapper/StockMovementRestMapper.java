package school.hei.pingpongspring.controller.mapper;

import org.springframework.stereotype.Component;
import school.hei.pingpongspring.controller.rest.StockMovementRest;
import school.hei.pingpongspring.model.StockMovement;

import java.util.function.Function;

@Component
public class StockMovementRestMapper implements Function<StockMovement, StockMovementRest> {

    @Override
    public StockMovementRest apply(StockMovement stockMovement) {
        return new StockMovementRest(stockMovement.getId(),
                stockMovement.getQuantity(),
                stockMovement.getUnit(),
                stockMovement.getType(),
                stockMovement.getDate());
    }
}
