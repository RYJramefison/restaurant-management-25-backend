package school.hei.pingpongspring.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class DishIngredient {
    private long dishId;
    private long ingredientId;
    private double requiredQuantity;
    private Unit unit;
}