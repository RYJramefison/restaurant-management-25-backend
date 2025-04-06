package school.hei.pingpongspring.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import school.hei.pingpongspring.repository.dao.StockMovementDAO;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Ingredient {
    private long id;
    private String name;
    private Instant dateTime;
    private Unit unit;
    private List<IngredientPrice> prices;
    private List<StockMovement> stockMovements;

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
}
