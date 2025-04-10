package school.hei.pingpongspring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.hei.pingpongspring.controller.mapper.DishRestMapper;
import school.hei.pingpongspring.controller.rest.DishRest;
import school.hei.pingpongspring.controller.rest.IngredientRest;
import school.hei.pingpongspring.model.Dish;
import school.hei.pingpongspring.model.Ingredient;
import school.hei.pingpongspring.model.StockMovement;
import school.hei.pingpongspring.service.DishService;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/dishes")
public class DishRestController {
    private final DishService dishService;
    private final DishRestMapper dishRestMapper;

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

    @PutMapping("/{id}/ingredients")
    public ResponseEntity<Object> updateingredientsInDish(@PathVariable Long id, @RequestBody List<Ingredient> ingredients) {
        List<Ingredient> ingredients1 = ingredients.stream()
                .map(ingredient ->
                        new Ingredient(ingredient.getId(), ingredient.getName(), ingredient.getDateTime(), ingredient.getUnit(),
                                ingredient.getPrices(),ingredient.getStockMovements()))
                .toList();
        Dish dish = dishService.addIngredients(id, ingredients1);
        Dish dishRest = dishRestMapper.toRest(dish);
        return ResponseEntity.ok().body(dishRest);
    }
}
