package school.hei.pingpongspring.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Dish {
    private long id;
    private String name;
    private int price;
    private List<Ingredient> ingredients;
}
