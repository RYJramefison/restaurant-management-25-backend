package school.hei.pingpongspring.controller.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import school.hei.pingpongspring.model.Unit;

@AllArgsConstructor
@Getter
public class CreateIngredient {
    private long id;
    private String name;
    private double requiredQuantity;
    private Unit unit;
}
