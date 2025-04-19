package school.hei.pingpongspring.controller.Antanimena;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import school.hei.pingpongspring.service.OrderService;

@Profile("Antanimena")
@RestController
@RequiredArgsConstructor
public class SalesRestController2 {
    private final OrderService orderService;
    @GetMapping("/Antanimena/sales")
    public ResponseEntity<Object> getSales(){
        return ResponseEntity.ok().body(orderService.GetSales());
    }
}
