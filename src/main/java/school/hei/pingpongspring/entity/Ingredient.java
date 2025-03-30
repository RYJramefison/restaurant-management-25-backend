package school.hei.pingpongspring.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Ingredient {
    private long id;
    private String name;
    private Double price;
    private Instant updatedAt;
}
