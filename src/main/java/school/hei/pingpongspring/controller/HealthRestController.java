package school.hei.pingpongspring.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import school.hei.pingpongspring.entity.Ingredient;

import java.time.Instant;
import java.util.List;

@RestController
public class HealthRestController {

    @GetMapping("/ping")
    public String pingPong(){
        return "pong";
    }

    public static List<Ingredient> getIngredients() {
        return List.of(
                new Ingredient(1L, "Oeuf", 1000.0, Instant.parse("2025-03-01T00:00:00Z")),
                new Ingredient(2L, "Huile", 10000.0, Instant.parse("2025-03-20T00:00:00Z"))
        );
    }

    @GetMapping("/ingredients")
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
        List<Ingredient> ingredients = getIngredients();
        List<Ingredient> filter = ingredients.stream().filter(ingredient -> {
            double price = ingredient.getPrice();
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


}
