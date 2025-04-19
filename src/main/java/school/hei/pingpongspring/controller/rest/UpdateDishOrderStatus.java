package school.hei.pingpongspring.controller.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import school.hei.pingpongspring.model.StatusOrder;

@AllArgsConstructor
@Getter
public class UpdateDishOrderStatus {
    private StatusOrder status;
}
