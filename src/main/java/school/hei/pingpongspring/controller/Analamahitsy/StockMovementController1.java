package school.hei.pingpongspring.controller.Analamahitsy;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import school.hei.pingpongspring.model.StockMovement;
import school.hei.pingpongspring.service.StockMovementService;

import java.util.List;
@Profile("Analamahitsy")

@RestController
@RequiredArgsConstructor
public class StockMovementController1 {
    private final StockMovementService stockMovementService;

    @GetMapping("/Analamahitsy/stockMovements")
    public ResponseEntity<List<StockMovement>> getAll(){
        return ResponseEntity.ok().body(stockMovementService.getAll());
    }
}
