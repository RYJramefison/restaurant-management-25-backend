package school.hei.pingpongspring.entity.test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class IngredientTest {
    private long id;
    private String name;
    private Double price;
    private Instant updatedAt;
}