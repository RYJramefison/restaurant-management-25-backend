package school.hei.pingpongspring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.hei.pingpongspring.controller.mapper.IngredientRestMapper;
import school.hei.pingpongspring.controller.rest.CreateIngredientPrice;
import school.hei.pingpongspring.controller.rest.CreateOrUpdateIngredient;
import school.hei.pingpongspring.controller.rest.IngredientRest;
import school.hei.pingpongspring.model.Ingredient;
import school.hei.pingpongspring.model.IngredientPrice;
import school.hei.pingpongspring.model.StockMovement;
import school.hei.pingpongspring.model.Unit;
import school.hei.pingpongspring.repository.dao.Criteria;
import school.hei.pingpongspring.service.IngredientService;
import school.hei.pingpongspring.service.exception.ClientException;
import school.hei.pingpongspring.service.exception.NotFoundException;

import java.rmi.ServerException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;


@RestController
@RequiredArgsConstructor
@RequestMapping("/ingredients")
public class IngredientRestController {
    private final IngredientService service;
    private final IngredientRestMapper ingredientRestMapper;

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable (name = "id", required = false) long id){
        return ResponseEntity.ok().body(service.findById(id));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Ingredient>> findByCriteria(
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Unit unit

    ){
        List<Criteria> criteria = new ArrayList<>();
        if (name != null) {
            criteria.add(new Criteria( "name" , name));
        }
        if (unit != null) {
            criteria.add(new Criteria( "unit" , unit));
        }
        return ResponseEntity.ok(service.findIngredientsByCriteria(criteria, size, page));
    }

    @GetMapping
    public ResponseEntity<Object> getIngredients(@RequestParam(name = "priceMinFilter", required = false) Double priceMinFilter,
                                                 @RequestParam(name = "priceMaxFilter", required = false) Double priceMaxFilter) {
        try {
            List<Ingredient> ingredientsByPrices = service.getIngredientsByPrices(priceMinFilter, priceMaxFilter);
            List<IngredientRest> ingredientRests = ingredientsByPrices.stream()
                    .map(ingredientRestMapper::toRest).toList();
            return ResponseEntity.ok().body(ingredientRests);
        } catch (ClientException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(e.getMessage());
        } catch (ServerException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping
    public ResponseEntity<Object> saveIngredients(@RequestBody List<CreateOrUpdateIngredient> ingredientsToCreateOrUpdate) {
        List<Ingredient> ingredients = ingredientsToCreateOrUpdate.stream()
                .map(ingredient -> ingredientRestMapper.toModel(ingredient))
                .toList();
        System.out.println("ingredients dans to model controller "+ingredients);
        List<IngredientRest> ingredientsRest = service.saveAll(ingredients).stream()
                .map(ingredient -> ingredientRestMapper.toRest(ingredient))
                .toList();
        System.out.println("ingredient dans rest controller"+ingredientsRest);
        return ResponseEntity.ok().body(ingredientsRest);
    }

    @PutMapping("/{ingredientId}/prices")
    public ResponseEntity<Object> updateIngredientPrices(@PathVariable Long ingredientId, @RequestBody List<CreateIngredientPrice> ingredientPrices) {
        List<IngredientPrice> prices = ingredientPrices.stream()
                .map(ingredientPrice ->
                        new IngredientPrice(ingredientPrice.getPrice(), ingredientPrice.getDate()))
                .toList();
        Ingredient ingredient = service.addPrices(ingredientId, prices);
        IngredientRest ingredientRest = ingredientRestMapper.toRest(ingredient);
        return ResponseEntity.ok().body(ingredientRest);
    }

    @PutMapping("/{ingredientId}/stockMovements")
    public ResponseEntity<Object> updateIngredientStockMovement(@PathVariable Long ingredientId, @RequestBody List<StockMovement> stockMovements) {
        List<StockMovement> stockMovements1 = stockMovements.stream()
                .map(stockMovement ->
                        new StockMovement(stockMovement.getIngredientId(), stockMovement.getType(), stockMovement.getQuantity(),
                                stockMovement.getUnit(),stockMovement.getDate()))
                .toList();
        Ingredient ingredient = service.addStockMovement(ingredientId, stockMovements1);
        IngredientRest ingredientRest = ingredientRestMapper.toRest(ingredient);
        return ResponseEntity.ok().body(ingredientRest);
    }


}
