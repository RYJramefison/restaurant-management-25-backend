package school.hei.pingpongspring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.hei.pingpongspring.controller.rest.AddOrUpdateDishOrders;
import school.hei.pingpongspring.controller.rest.UpdateDishOrderStatus;
import school.hei.pingpongspring.model.DishOrder;
import school.hei.pingpongspring.model.DishOrderStatus;
import school.hei.pingpongspring.model.Order;
import school.hei.pingpongspring.service.OrderService;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderRestController {
    private final OrderService orderService;

    @GetMapping("/{reference}")
    public ResponseEntity<Object> findByReference(@PathVariable (required = false) String reference){
        return ResponseEntity.ok().body(orderService.findByReference(reference));
    }

    @PostMapping("/{reference}")
    public ResponseEntity<Object> saveOrder(@PathVariable (required = false) String reference) throws SQLException {
        Order order = new Order(reference);
        return ResponseEntity.ok().body(orderService.saveOrder(order));
    }

    @GetMapping("/dishes/{id}")
    public ResponseEntity<Object> findDishesById(@PathVariable (required = false) long id){
        return ResponseEntity.ok().body(orderService.getdishByOrder(id));
    }

    @PutMapping("/{reference}/dishes")
    public ResponseEntity<Object> updateDishOrder(
            @PathVariable (required = false) long reference,
            @RequestBody List<AddOrUpdateDishOrders> dishOrderList
    ) throws Exception {
        return ResponseEntity.ok().body(orderService.updateDishOrder(reference, dishOrderList));
    }

    @PutMapping("/{reference}/dishes/{dishId}")
    public ResponseEntity<Object> updateDishStatus(
            @PathVariable (required = false) long reference,
            @PathVariable (required = false) long dishId,
            @RequestBody UpdateDishOrderStatus dishOrderStatus
            ) {
        return ResponseEntity.ok().body(orderService.updateDishOrderStatus(reference,dishId,dishOrderStatus));
    }



}
