package school.hei.pingpongspring.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import school.hei.pingpongspring.entity.StockMovement;
import school.hei.pingpongspring.service.StockMovementService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StockMovementController {
    private final StockMovementService stockMovementService;

    @GetMapping("/stockMovements")
    public ResponseEntity<List<StockMovement>> getAll(){
        return ResponseEntity.ok().body(stockMovementService.getAll());
    }
}
