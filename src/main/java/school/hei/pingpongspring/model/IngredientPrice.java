package school.hei.pingpongspring.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class IngredientPrice {
    private long id;
    private long ingredientId;
    private double price;
    private Instant date;

    public IngredientPrice(double price) {
        this.price = price;
    }
}
