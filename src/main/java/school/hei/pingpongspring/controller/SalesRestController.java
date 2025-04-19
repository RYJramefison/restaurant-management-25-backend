package school.hei.pingpongspring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import school.hei.pingpongspring.service.OrderService;

@RestController
@RequiredArgsConstructor
public class SalesRestController {
    private final OrderService orderService;
    @GetMapping("/sales")
    public ResponseEntity<Object> getSales(){
        return ResponseEntity.ok().body(orderService.GetSales());
    }
}
