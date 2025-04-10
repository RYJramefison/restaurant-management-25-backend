package school.hei.pingpongspring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import school.hei.pingpongspring.service.OrderService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderRestController {
    private final OrderService orderService;

    @GetMapping("/{reference}")
    public ResponseEntity<Object> findById(@PathVariable (required = false) long reference){
        return ResponseEntity.ok().body(orderService.findById(reference));
    }
}
