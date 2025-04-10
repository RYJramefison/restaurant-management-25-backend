package school.hei.pingpongspring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

    @GetMapping("/ingredients/{id}")
    public ResponseEntity<Object> getIngredientsByDish(@PathVariable (required = false) long id) {
        return ResponseEntity.ok().body(dishService.getIngredientByDish(id));
    }
}
