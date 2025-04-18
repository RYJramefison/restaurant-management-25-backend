package school.hei.pingpongspring.controller.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import school.hei.pingpongspring.model.IngredientPrice;
import school.hei.pingpongspring.model.StockMovement;
import school.hei.pingpongspring.model.Unit;

import java.time.Instant;
import java.util.List;

@AllArgsConstructor
@Getter
public class CreateOrUpdateDishIngredient {
    private Long id;
    private String name;
    private Unit unit;
    private Double requiredQuantity;
}
