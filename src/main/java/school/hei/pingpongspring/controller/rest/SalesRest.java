package school.hei.pingpongspring.controller.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SalesRest {
    private long dishId;
    private String dishName;
    private String salesPoint;
    private int quantitySold;
    private int price;

    public SalesRest(long dishId, String dishName, int quantitySold, int price) {
        this.dishId = dishId;
        this.dishName = dishName;
        this.quantitySold = quantitySold;
        this.price = price;
    }
}
