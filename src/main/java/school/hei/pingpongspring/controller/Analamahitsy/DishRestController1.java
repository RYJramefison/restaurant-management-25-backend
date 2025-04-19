package school.hei.pingpongspring.controller.Analamahitsy;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.hei.pingpongspring.controller.mapper.DishRestMapper;
import school.hei.pingpongspring.controller.rest.CreateDish;
import school.hei.pingpongspring.controller.rest.CreateOrUpdateDishIngredient;
import school.hei.pingpongspring.model.Dish;
import school.hei.pingpongspring.service.DishService;

import java.util.List;

@Profile("Analamahitsy")

@RestController
@RequiredArgsConstructor
@RequestMapping("/Analamahitsy/dishes")
public class DishRestController1 {
    private final DishService dishService;
    private final DishRestMapper dishRestMapper;

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable (required = false) long id) {
        return ResponseEntity.ok().body(dishService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Object> saveDish(@RequestBody List<CreateDish> dish) {
        return ResponseEntity.ok().body(dishService.saveAllDishes(dish));
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
    public ResponseEntity<Object> updateIngredientsInDish(@PathVariable Long id, @RequestBody List<CreateOrUpdateDishIngredient> ingredients) {
        List<CreateOrUpdateDishIngredient> ingredients1 = ingredients.stream()
                .map(ingredient ->
                        new CreateOrUpdateDishIngredient(ingredient.getId(), ingredient.getName(), ingredient.getUnit(), ingredient.getRequiredQuantity()))
                .toList();
        Dish dish = dishService.addIngredients(id, ingredients1);
        Dish dishRest = dishRestMapper.toRest(dish);
        return ResponseEntity.ok().body(dishRest);
    }
}
