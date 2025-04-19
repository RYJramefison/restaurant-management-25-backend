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
    private String name;
    private String salePoint;
    private int quantitySold;
    private int price;

    public SalesRest(long dishId, String name, int quantitySold, int price) {
        this.dishId = dishId;
        this.name = name;
        this.quantitySold = quantitySold;
        this.price = price;
    }
}
