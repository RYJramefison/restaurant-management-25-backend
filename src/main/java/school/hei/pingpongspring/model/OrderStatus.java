package school.hei.pingpongspring.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
@Data
@NoArgsConstructor

public class OrderStatus {
    private long id;
    private Instant dateTime;
    private StatusOrder statusOrder;
    private long orderId;

    public OrderStatus(Instant dateTime, StatusOrder statusOrder, long orderId) {
        this.dateTime = dateTime;
        this.statusOrder = statusOrder;
        this.orderId = orderId;
    }
}
