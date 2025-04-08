package school.hei.pingpongspring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import school.hei.pingpongspring.model.Ingredient;
import school.hei.pingpongspring.model.IngredientPrice;
import school.hei.pingpongspring.repository.dao.Criteria;
import school.hei.pingpongspring.repository.dao.IngredientDAO;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IngredientService {
    private final IngredientDAO subjectIngredient;

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

    public Ingredient addPrices(Long ingredientId, List<IngredientPrice> pricesToAdd) {
        Ingredient ingredient = subjectIngredient.findById(ingredientId);
        ingredient.addPrices(pricesToAdd);
        List<Ingredient> ingredientsSaved = subjectIngredient.saveAll(List.of(ingredient));
        return ingredientsSaved.getFirst();
    }
}
