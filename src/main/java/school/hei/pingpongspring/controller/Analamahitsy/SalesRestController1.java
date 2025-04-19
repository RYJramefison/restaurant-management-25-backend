package school.hei.pingpongspring.controller.Analamahitsy;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import school.hei.pingpongspring.controller.rest.SalesRest;
import school.hei.pingpongspring.service.OrderService;

import java.util.List;

@Profile("Analamahitsy")

@RestController
@RequiredArgsConstructor
public class SalesRestController1 {
    private final OrderService orderService;
    @GetMapping("/Analamahitsy/sales")
    public ResponseEntity<Object> getSales(){
        List<SalesRest> salesRestList = orderService.GetSales();
        for (SalesRest salesRest : salesRestList) {
            salesRest.setSalePoint("Analamahitsy");
        }
        return ResponseEntity.ok().body(salesRestList);
    }
}
