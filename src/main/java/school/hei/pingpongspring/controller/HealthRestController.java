package school.hei.pingpongspring.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.hei.pingpongspring.model.test.IngredientTest;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RestController
public class HealthRestController {

    @GetMapping("/ping")
    public String pingPong(){
        return "pong";
    }

    public static List<IngredientTest> getIngredientsTest() {
        return List.of(
                new IngredientTest(1L, "Oeuf", 1000.0, Instant.parse("2025-03-01T00:00:00Z")),
                new IngredientTest(2L, "Huile", 10000.0, Instant.parse("2025-03-20T00:00:00Z"))
        );
    }

    @GetMapping("/ingredientsTest")
    public ResponseEntity<Object> findAllIngredients(
            @RequestParam (name = "priceMinFilter",required = false) Double priceMinFilter,
            @RequestParam (name = "priceMaxFilter",required = false) Double priceMaxFilter
            ) {
        if (priceMinFilter != null && priceMinFilter < 0) {
            return new ResponseEntity<>("priceMinFilter " + priceMinFilter + " is negatif", HttpStatus.BAD_REQUEST);
        }
        if (priceMaxFilter != null && priceMaxFilter < 0) {
            return new ResponseEntity<>("priceMaxFilter " + priceMaxFilter + " is negatif", HttpStatus.BAD_REQUEST);
        }
        if (priceMinFilter != null && priceMinFilter !=null) {
            if (priceMaxFilter < priceMinFilter){
                return new ResponseEntity<>("priceMinFilter " + priceMinFilter + " is greater than priceMaxFilter " + priceMaxFilter, HttpStatus.BAD_REQUEST);

            }
        }
        List<IngredientTest> ingredientTests = getIngredientsTest()
                ;
        List<IngredientTest> filter = ingredientTests.stream().filter(ingredientTest -> {
            double price = ingredientTest.getPrice();
            if (priceMinFilter != null && priceMaxFilter == null){
                return price >= priceMinFilter;
            }
            if (priceMinFilter == null && priceMaxFilter != null){
                return price <= priceMaxFilter;

            }
            if (priceMinFilter != null && priceMaxFilter != null){
                return price >= priceMinFilter && price <= priceMaxFilter;
            }
            return true;
        }).toList();

        return ResponseEntity.ok().body(filter);
    }

    @PostMapping("/ingredientsTest")
    public ResponseEntity<Object> saveIngredients(@RequestBody List<IngredientTest> ingredientTests){
        return ResponseEntity.status(HttpStatus.CREATED).body(ingredientTests);
    }

    @PutMapping("/ingredientsTest")
    public ResponseEntity<Object> updateIngredient(@RequestBody List<IngredientTest> ingredientTests){
        return ResponseEntity.status(HttpStatus.OK).body(ingredientTests);
    }

    @GetMapping("/ingredientsTest/{id}")
    public ResponseEntity<Object> getIngredientById(
            @PathVariable (name = "id", required = false) int id){
        List<IngredientTest> ingredientTests = getIngredientsTest();
        Optional<IngredientTest> ingredient = ingredientTests.stream().filter(ingredientTest1 -> ingredientTest1.getId() == id).findAny();
        if (ingredient.isPresent()){
            return ResponseEntity.ok().body(ingredient);
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ingredient = "+ id + " id not found");
        }
    }



}
