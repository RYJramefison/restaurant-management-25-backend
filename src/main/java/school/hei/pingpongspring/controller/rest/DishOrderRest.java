package school.hei.pingpongspring.controller.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DishOrderRest {
    private long dishIdentifier;
    private int quantityOrdered;
}
