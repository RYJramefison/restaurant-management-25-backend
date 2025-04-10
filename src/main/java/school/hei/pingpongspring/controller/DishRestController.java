package school.hei.pingpongspring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.hei.pingpongspring.service.DishService;


@RestController
@RequiredArgsConstructor
@RequestMapping("/dishes")
public class DishRestController {
    private final DishService dishService;

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable (required = false) long id) {
        return ResponseEntity.ok().body(dishService.findById(id));
    }

    @GetMapping
    public ResponseEntity<Object> getAll(
            @RequestParam(defaultValue = "1", required = false)  int page,
            @RequestParam (defaultValue = "10", required = false) int size
    ) {
        return ResponseEntity.ok().body(dishService.getAll(page, size));
    }

    @GetMapping("/ingredients/{id}")
    public ResponseEntity<Object> getIngredientsByDish(@PathVariable (required = false) long id) {
        return ResponseEntity.ok().body(dishService.getIngredientByDish(id));
    }
}
