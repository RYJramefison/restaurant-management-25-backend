package school.hei.pingpongspring.controller.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class DishRest {
    private Long id;
    private String name;
    private Integer availableQuantity;
    private Double price;
    private List<DishIngredientRest> ingredients;
}
