package school.hei.pingpongspring.controller.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import school.hei.pingpongspring.model.DishOrder;
import school.hei.pingpongspring.model.StatusOrder;

import java.util.List;

@AllArgsConstructor
@Getter
public class AddOrUpdateDishOrders {
    private StatusOrder orderStatus;
    private List<DishOrderRest> dishes;
}
