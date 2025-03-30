package school.hei.pingpongspring.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.hei.pingpongspring.entity.Ingredient;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
public class IngredientRestController {
    @GetMapping("/ingredientss")
    public ResponseEntity<Object> getIngredients(@RequestParam(name = "priceMinFilter", required = false) Double priceMinFilter,
                                                 @RequestParam(name = "priceMaxFilter", required = false) Double priceMaxFilter) {
        if (priceMinFilter != null && priceMinFilter < 0) {
            return new ResponseEntity<>("PriceMinFilter " + priceMinFilter + " is negative", HttpStatus.BAD_REQUEST);
        }
        if (priceMaxFilter != null && priceMaxFilter < 0) {
            return new ResponseEntity<>("PriceMaxFilter " + priceMaxFilter + " is negative", HttpStatus.BAD_REQUEST);
        }
        if (priceMinFilter != null && priceMaxFilter != null) {
            if (priceMinFilter > priceMaxFilter) {
                return ResponseEntity.badRequest()
                        .body("PriceMinFilter " + priceMinFilter + " is greater than PriceMaxFilter " + priceMaxFilter);
            }
        }
        List<Ingredient> ingredients = getIngredientList();
        List<Ingredient> filteredIngredients = ingredients.stream()
                .filter(ingredient -> {
                    if (priceMinFilter == null && priceMaxFilter == null) {
                        return true;
                    }
                    Double unitPrice = ingredient.getPrice();
                    if (priceMinFilter != null && priceMaxFilter == null) {
                        return unitPrice >= priceMinFilter;
                    }
                    if (priceMinFilter == null) {
                        return unitPrice <= priceMaxFilter;
                    }
                    return unitPrice >= priceMinFilter && unitPrice <= priceMaxFilter;
                })
                .toList();
        return ResponseEntity.ok().body(filteredIngredients);
    }

    @PostMapping("/ingredientss")
    public ResponseEntity<Object> addIngredients(@RequestBody List<Ingredient> ingredients) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ingredients);
    }

    @PutMapping("/ingredientss")
    public ResponseEntity<Object> updateIngredients(@RequestBody List<Ingredient> ingredients) {
        return ResponseEntity.status(HttpStatus.OK).body(ingredients);
    }

    private static List<Ingredient> getIngredientList() {
        return List.of(
                new Ingredient(1L, "Oeuf", 1000.0, Instant.parse("2025-03-01T00:00:00Z")),
                new Ingredient(2L, "Huile", 10000.0, Instant.parse("2025-03-20T00:00:00Z"))
        );
    }

    @GetMapping("/ingredientss/{id}")
    public ResponseEntity<Object> getIngredient(@PathVariable Long id) {
        Optional<Ingredient> optionalIngredient = getIngredientList().stream().filter(ingredient -> Objects.equals(ingredient.getId(), id))
                .findAny();
        if (optionalIngredient.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(optionalIngredient.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ingredient=" + id + " not found");
    }
}