package school.hei.pingpongspring.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor

public class DishOrder {
    private long id;
    private Dish dish;
    private long orderId;
    private int quantity;
    @JsonIgnore
    private List<DishOrderStatus> status;



    public StatusOrder getActualStatus(){
        DishOrderStatus dishOrderStatus = this.getStatus().stream()
                .max(Comparator.comparing(DishOrderStatus::getDateTime))
                .orElse(new DishOrderStatus(Instant.now(), StatusOrder.CREATE,this.id));

        return dishOrderStatus.getStatusOrder();
    }

    public DishOrder(long id, Dish dish, int quantity) {
        this.id = id;
        this.dish = dish;
        this.quantity = quantity;
    }
}
