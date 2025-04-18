package school.hei.pingpongspring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import school.hei.pingpongspring.model.Ingredient;
import school.hei.pingpongspring.model.IngredientPrice;
import school.hei.pingpongspring.model.StockMovement;
import school.hei.pingpongspring.repository.dao.Criteria;
import school.hei.pingpongspring.repository.dao.IngredientDAO;
import school.hei.pingpongspring.repository.dao.IngredientPriceDAO;
import school.hei.pingpongspring.repository.dao.StockMovementDAO;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IngredientService {
    private final IngredientDAO subjectIngredient;
    private final IngredientPriceDAO subjectPrice;
    private final StockMovementDAO subjectStockMovement;

    public Ingredient findById(long id){
        return subjectIngredient.findById(id);
    }

    public List<Ingredient> getIngredientsByPrices(Double priceMinFilter, Double priceMaxFilter) throws Exception {
        if (priceMinFilter != null && priceMinFilter < 0) {
            throw new Exception("PriceMinFilter " + priceMinFilter + " is negative");
        }
        if (priceMaxFilter != null && priceMaxFilter < 0) {
            throw new Exception("PriceMaxFilter " + priceMaxFilter + " is negative");
        }
        if (priceMinFilter != null && priceMaxFilter != null) {
            if (priceMinFilter > priceMaxFilter) {
                throw new Exception("PriceMinFilter " + priceMinFilter + " is greater than PriceMaxFilter " + priceMaxFilter);
            }
        }
        List<Criteria> criteria = new ArrayList<>();

        // TODO : paginate from restController OR filter from repository directly
        List<Ingredient> ingredients = subjectIngredient.findIngredientByCriteria(criteria,500, 1);
        System.out.println(ingredients);
        return ingredients.stream()
                .filter(ingredient -> {
                    if (priceMinFilter == null && priceMaxFilter == null) {
                        return true;
                    }
                    Double unitPrice = ingredient.getActualPrice();
                    if (priceMinFilter != null && priceMaxFilter == null) {
                        return unitPrice >= priceMinFilter;
                    }
                    if (priceMinFilter == null) {
                        return unitPrice <= priceMaxFilter;
                    }
                    return unitPrice >= priceMinFilter && unitPrice <= priceMaxFilter;
                })
                .toList();
    }

    public List<Ingredient> findIngredientsByCriteria(List<Criteria> criteria , int size, int page){
        return subjectIngredient.findIngredientByCriteria(criteria , size, page);
    }

    public List<Ingredient> saveAll(List<Ingredient> ingredients) {
        return subjectIngredient.saveAll(ingredients);
    }

    public List<Ingredient> updateAll(List<Ingredient> ingredients) {
        return subjectIngredient.updateAll(ingredients);
    }

    public Ingredient addPrices(Long ingredientId, List<IngredientPrice> pricesToAdd) {
        Ingredient ingredient = subjectIngredient.findById(ingredientId);
        pricesToAdd.forEach(p -> p.setIngredientId(ingredientId));

        List<IngredientPrice> savedPrices = subjectPrice.saveAll(pricesToAdd);
        ingredient.addPrices(savedPrices); // mise à jour de la liste avec les prix réellement enregistrés

        return ingredient;
    }


    public Ingredient addStockMovement(Long ingredientId, List<StockMovement> stockMovementsToAdd) {
        Ingredient ingredient = subjectIngredient.findById(ingredientId);
        stockMovementsToAdd.forEach(p -> p.setIngredientId(ingredientId));

        List<StockMovement> SavedStockMovement = subjectStockMovement.saveAll(stockMovementsToAdd);

        ingredient.addStockMovements(SavedStockMovement);
        return ingredient;
    }
}