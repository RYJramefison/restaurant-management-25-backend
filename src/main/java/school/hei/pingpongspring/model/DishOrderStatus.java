package school.hei.pingpongspring.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
@Data
@NoArgsConstructor

public class DishOrderStatus {
    private long id;
    private Instant dateTime;
    private StatusOrder statusOrder;
    private long dishOrderId;


    public DishOrderStatus(Instant dateTime, StatusOrder statusOrder, long dishOrderId) {
        this.dateTime = dateTime;
        this.statusOrder = statusOrder;
        this.dishOrderId = dishOrderId;
    }

}
