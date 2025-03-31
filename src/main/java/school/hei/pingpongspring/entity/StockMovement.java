package school.hei.pingpongspring.entity;

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
}
