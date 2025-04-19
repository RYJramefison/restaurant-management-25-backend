package school.hei.pingpongspring.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import school.hei.pingpongspring.controller.rest.DishRest;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Dish {
    private long id;
    private String name;
    private int price;
    private List<Ingredient> ingredients = new ArrayList<>();

    public Integer getIngredientCost(){

        return this.ingredients.stream().mapToInt(ingredient -> {
            List<IngredientPrice> prices = ingredient.getPrices();

            Instant latestDate = prices.stream().map(IngredientPrice::getDate).max(Instant::compareTo).orElse(null);

            if (latestDate != null) {
                return (int) prices.stream().filter(price -> price.getDate().equals(latestDate) && price.getIngredientId() == ingredient.getId())
                        .mapToDouble(IngredientPrice::getPrice).findFirst().orElse(0);
            }
            return 0;
        }).sum();
    }

    public Dish(long id, String name, int price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Integer getIngredientCost(LocalDate expectedDate){

        return this.ingredients.stream().mapToInt(ingredient -> {
            List<IngredientPrice> prices = ingredient.getPrices();

            return (int) prices.stream().filter(price -> price.getIngredientId() == ingredient.getId() && price.getDate().equals(expectedDate))
                    .mapToDouble(IngredientPrice::getPrice).findFirst().orElse(0);
        }).sum();
    }

    public double getGrossMargin(){
        double totalGrossMargin = this.price - getIngredientCost();

        return totalGrossMargin;
    }
    public double getGrossMargin(LocalDate date){
        double totalGrossMargin = this.price - getIngredientCost(date);

        return totalGrossMargin;
    }

    public List<Ingredient> addIngredients(List<Ingredient> ingredientsToAdd) {
        if (ingredientsToAdd == null || ingredientsToAdd.isEmpty()){
            return getIngredients();
        }

        if (getIngredients() == null){
            this.setIngredients(new ArrayList<>());
        }
        getIngredients().addAll(ingredientsToAdd);
        return getIngredients();
    }

}
