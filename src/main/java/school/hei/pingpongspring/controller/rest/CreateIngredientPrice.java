package school.hei.pingpongspring.controller.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.Instant;

@AllArgsConstructor
@Getter
public class CreateIngredientPrice {
    private Double price;
    private Instant date;
}
