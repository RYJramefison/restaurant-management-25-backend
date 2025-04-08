package school.hei.pingpongspring.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class StockMovement {
    private long id;
    private long ingredientId;
    private MovementType type;
    private double quantity;
    private Unit unit;
    private Instant date;


    public StockMovement(long ingredientId, MovementType type, double quantity, Unit unit, Instant date) {
        this.ingredientId = ingredientId;
        this.type = type;
        this.quantity = quantity;
        this.unit = unit;
        this.date = date;
    }
}