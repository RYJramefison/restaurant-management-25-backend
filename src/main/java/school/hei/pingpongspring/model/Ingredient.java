package school.hei.pingpongspring.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import school.hei.pingpongspring.repository.dao.IngredientPriceDAO;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Ingredient {
    private long id;
    private String name;
    private Instant dateTime;
    private Unit unit;
    @JsonIgnore
    private List<IngredientPrice> prices = new ArrayList<>();
    @JsonIgnore
    private List<StockMovement> stockMovements = new ArrayList<>();

    public Ingredient(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Ingredient(long id, String name, Unit unit) {
        this.id = id;
        this.name = name;
        this.unit = unit;
    }

    public Ingredient(String name, Unit unit) {
        this.name = name;
        this.unit = unit;
    }

    public double getAvailableQuantity(){
        Instant latestDate = Instant.now();
        double quantity = stockMovements.stream().filter(stockMovement ->
                stockMovement.getIngredientId() == this.getId() && (stockMovement.getDate().isBefore(latestDate) || stockMovement.getDate().equals(latestDate))).mapToDouble(stockMovement -> {
            if (stockMovement.getType() == MovementType.IN) {
                return stockMovement.getQuantity();
            }
            else {
                return - stockMovement.getQuantity();
            }
        }).sum();

        return quantity;

    }
    public double getAvailableQuantity(Instant dateTime){
        double quantity =  stockMovements.stream().filter(stockMovement ->
                        stockMovement.getIngredientId() == this.getId() && (stockMovement.getDate().isBefore(dateTime) || stockMovement.getDate().equals(dateTime)) )
                .mapToDouble(stockMovement -> {
                    if (stockMovement.getType() == MovementType.IN) {
                        return stockMovement.getQuantity();
                    }
                    else {
                        return -stockMovement.getQuantity();
                    }
                }).sum();

        return quantity;
    }

    public Double getActualPrice() {
        return findActualPrice().orElse(new IngredientPrice(0.0)).getPrice();
    }

    private Optional<IngredientPrice> findActualPrice() {
        return this.prices.stream().max(Comparator.comparing(IngredientPrice::getDate));
    }

    public Double getPriceAt(Instant dateValue) {
        return findPriceAt(dateValue).orElse(new IngredientPrice(0.0)).getPrice();
    }

    private Optional<IngredientPrice> findPriceAt(Instant dateValue) {
        return prices.stream()
                .filter(price -> price.getDate().equals(dateValue))
                .findFirst();
    }

    public List<StockMovement> addStockMovements(List<StockMovement> stockMovements) {
        if (stockMovements == null || stockMovements.isEmpty()){
            return getStockMovements();
        }
        stockMovements.forEach(stockMovement -> stockMovement.setIngredientId(this.getId()));

        if (getStockMovements() == null){
            this.setStockMovements(new ArrayList<>());
        }
        getStockMovements().addAll(stockMovements);
        return getStockMovements();
    }

    public List<IngredientPrice> addPrices(List<IngredientPrice> prices) {
        if (prices == null || prices.isEmpty()){
            return getPrices();
        }
        prices.forEach(price -> price.setIngredientId(this.getId()));

        if (getPrices() == null) {
            this.setPrices(new ArrayList<>());
        }
        getPrices().addAll(prices);
        return getPrices();
    }


}
