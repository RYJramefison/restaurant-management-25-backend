package school.hei.pingpongspring.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class IngredientPrice {
    private long id;
    @JsonIgnore
    private long ingredientId;
    private double price;
    private Instant date;

    public IngredientPrice(double price) {
        this.price = price;
    }

    public IngredientPrice(double price, Instant date) {
        this.price = price;
        this.date = date;
    }
}
