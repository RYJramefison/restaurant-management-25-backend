package school.hei.pingpongspring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.hei.pingpongspring.model.DishOrder;
import school.hei.pingpongspring.model.DishOrderStatus;
import school.hei.pingpongspring.service.OrderService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderRestController {
    private final OrderService orderService;

    @GetMapping("/{reference}")
    public ResponseEntity<Object> findById(@PathVariable (required = false) long reference){
        return ResponseEntity.ok().body(orderService.findById(reference));
    }

    @GetMapping("/dishes/{id}")
    public ResponseEntity<Object> findDishesById(@PathVariable (required = false) long id){
        return ResponseEntity.ok().body(orderService.getdishByOrder(id));
    }

    @PutMapping("/{reference}/dishes")
    public ResponseEntity<Object> updateDishOrder(
            @PathVariable (required = false) long reference,
            @RequestBody List<DishOrder> dishOrderList
    ) throws Exception {
        return ResponseEntity.ok().body(orderService.updateDishOrder(dishOrderList));
    }

    @PutMapping("/{reference}/dishes/{dishId}")
    public ResponseEntity<Object> updateDishStatus(
            @PathVariable (required = false) long reference,
            @PathVariable (required = false) long dishId,
            @RequestBody DishOrderStatus dishOrderStatus
            ) {
        return ResponseEntity.ok().body(orderService.updateDishOrderStatus(reference,dishId,dishOrderStatus));
    }


}
