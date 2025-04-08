package school.hei.pingpongspring.controller.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import school.hei.pingpongspring.model.Unit;

@AllArgsConstructor
@Getter
public class DishIngredientRest {
    private IngredientBasicProperty ingredient;
    private Double requiredQuantity;
    private Unit unit;
}
