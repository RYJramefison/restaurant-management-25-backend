package school.hei.pingpongspring.controller.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateDish {
    private long id;
    private String name;
    private int price;
}
