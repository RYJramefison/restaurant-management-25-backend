package school.hei.pingpongspring.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Ingredient {
    private long id;
    private String name;
    private Instant dateTime;
    private Unit unit;
    private List<Price> prices;
    private List<StockMovement> stockMovements;
}
