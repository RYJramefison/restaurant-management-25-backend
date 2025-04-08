package school.hei.pingpongspring.controller.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import school.hei.pingpongspring.model.MovementType;
import school.hei.pingpongspring.model.Unit;

import java.time.Instant;

@AllArgsConstructor
@Getter
public class StockMovementRest {
    private Long id;
    private Double quantity;
    private Unit unit;
    private MovementType type;
    private Instant creationDatetime;
}
