package school.hei.pingpongspring.controller.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class IngredientRest {
    private Long id;
    private String name;
    private List<PriceRest> prices;
    private List<StockMovementRest> stockMovements;
}
