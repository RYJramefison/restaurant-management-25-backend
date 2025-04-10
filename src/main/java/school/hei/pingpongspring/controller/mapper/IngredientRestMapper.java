package school.hei.pingpongspring.controller.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import school.hei.pingpongspring.controller.rest.CreateOrUpdateIngredient;
import school.hei.pingpongspring.controller.rest.IngredientRest;
import school.hei.pingpongspring.controller.rest.PriceRest;
import school.hei.pingpongspring.controller.rest.StockMovementRest;
import school.hei.pingpongspring.model.Ingredient;
import school.hei.pingpongspring.repository.dao.IngredientDAO;
import school.hei.pingpongspring.service.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@Component
public class IngredientRestMapper {
    @Autowired private PriceRestMapper priceRestMapper;
    @Autowired private StockMovementRestMapper stockMovementRestMapper;
    @Autowired private IngredientDAO subjectIngredients;

    public IngredientRest toRest(Ingredient ingredient) {
        List<PriceRest> prices = ingredient.getPrices().stream()
                .map(price -> priceRestMapper.apply(price)).toList();
        List<StockMovementRest> stockMovementRests = ingredient.getStockMovements().stream()
                .map(stockMovement -> stockMovementRestMapper.apply(stockMovement))
                .toList();
        return new IngredientRest(ingredient.getId(), ingredient.getName(), ingredient.getDateTime(), ingredient.getUnit(), prices, stockMovementRests);
    }

    public Ingredient toModel(CreateOrUpdateIngredient newIngredient) {
        Ingredient ingredient = new Ingredient();
        if (newIngredient.getId() != null) {
            ingredient.setId(newIngredient.getId());
        }
        ingredient.setName(newIngredient.getName());
        ingredient.setDateTime(newIngredient.getDateTime());
        ingredient.setUnit(newIngredient.getUnit());
        ingredient.setPrices(newIngredient.getPrices());
        ingredient.setStockMovements(newIngredient.getStockMovements());

        System.out.println("voici les resultat dans to model "+ingredient);
        return ingredient;
    }

//    public IngredientRest apply(Ingredient ingredient) {
//        return new IngredientRest(ingredient.getId(),ingredient.getName(), ingredient.getDateTime(), ingredient.getUnit(),ingredient.getPrices(),ingredient.getStockMovements());
//
//    }

}
