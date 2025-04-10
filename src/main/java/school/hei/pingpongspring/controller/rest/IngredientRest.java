package school.hei.pingpongspring.controller.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import school.hei.pingpongspring.model.Unit;

import java.time.Instant;
import java.util.List;

@AllArgsConstructor
@Getter
public class IngredientRest {
    private Long id;
    private String name;
    private Instant dateTime;
    private Unit unit;
    private List<PriceRest> prices;
    private List<StockMovementRest> stockMovements;
}
