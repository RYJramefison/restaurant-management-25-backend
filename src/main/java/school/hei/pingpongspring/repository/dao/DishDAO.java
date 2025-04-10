package school.hei.pingpongspring.repository.dao;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import school.hei.pingpongspring.model.*;
import school.hei.pingpongspring.repository.bd.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DishDAO implements CrudDAO<Dish> {
    private final DataSource db;
    private final IngredientDAO ingredientCrud ;
    private final Dish_IngredientDAO dish_ingredientCrud ;
    private final IngredientPriceDAO subjectPrice;
    private final StockMovementDAO subjectStockMovement;

    public List<Dish> getAll(int page, int size){
        List<Dish> dishes = new ArrayList<>();
        String sql = "SELECT * from dish LIMIT ? OFFSET ?";
        try (Connection connection = db.getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)){
            pstm.setInt(1, size);
            pstm.setInt(2, size * (page - 1));
            try (ResultSet res = pstm.executeQuery()){
                while (res.next()){
                    Dish dish = new Dish();
                    List<Ingredient> ingredients = findIngredientByDish(res.getInt("id"));
                    dish.setId(res.getInt("id"));
                    dish.setName(res.getString("name"));
                    dish.setPrice(res.getInt("price"));
                    dish.setIngredients(ingredients);
                    dishes.add(dish);
                }
                return dishes;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Not implemented" , e);
        }
    }


    @Override
    public Dish findById(long id) {
        String sql = "SELECT * from dish WHERE id =?";

        try (Connection connection = db.getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)){
            pstm.setInt(1, (int) id);
            try (ResultSet res = pstm.executeQuery()){
                List<Ingredient> ingredients = findIngredientByDish(id);
                Dish dish = new Dish();
                while (res.next()){
                    dish.setId(res.getInt("id"));
                    dish.setName(res.getString("name"));
                    dish.setPrice(res.getInt("price"));
                    dish.setIngredients(ingredients);
                }
                return dish;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Not implemented" , e);
        }
    }

    @Override
    public void save(Dish toSave) {
        String sql = "INSERT INTO dish (id, name, price) VALUES (?,?,?)" +
                " ON CONFLICT (id) DO UPDATE SET name=excluded.name, price=excluded.price" +
                "return id, name, price";
        boolean exist= false;
        try (Connection connection = db.getConnection()){
                try (PreparedStatement pstm = connection.prepareStatement(sql)){
                pstm.setInt(1, (int) toSave.getId());
                pstm.setString(2, toSave.getName());
                pstm.setInt(3, toSave.getPrice());

                pstm.executeUpdate();

                toSave.getIngredients().forEach(ingredient -> ingredientCrud.save(ingredient));

                    for (Ingredient ingredient : toSave.getIngredients()) {
                        DishIngredient dishIngredient = new DishIngredient();
                        int dishId = (int) toSave.getId();
                        int ingredientId = (int) ingredient.getId();
                        float requiredQuantity = 0;
                        double recentPrice = ingredient.getPrices().getLast().getPrice();
                        if (ingredient.getUnit() == Unit.G) {
                            requiredQuantity = (float)  recentPrice/ 20;
                        }
                        if (ingredient.getUnit() == Unit.L) {
                            requiredQuantity = (float) recentPrice / 10000;
                        }
                        if (ingredient.getUnit() == Unit.U) {
                            requiredQuantity = (float) recentPrice / 1000;
                        }
                        Unit unit = ingredient.getUnit();
                        dishIngredient.setDishId(dishId);
                        dishIngredient.setIngredientId(ingredientId);
                        dishIngredient.setRequiredQuantity(requiredQuantity);
                        dishIngredient.setUnit(unit);
                        dish_ingredientCrud.save(dishIngredient);
                    }
                }



        } catch (SQLException e){
            throw new RuntimeException("Not implemented", e);
        }
    }



    public List<Ingredient> findIngredientByDish(long idDish){
        String sql = "SELECT i.id, i.name, i.datetime, i.unit from ingredient i inner join dish_ingredient di on di.ingredient_id = i.id where di.dish_id=?";

        try (Connection connection = db.getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)){
            pstm.setInt(1, (int) idDish);

            try (ResultSet res = pstm.executeQuery()){
                List<Ingredient> ingredients = new ArrayList<>();
                while (res.next()){
                    Ingredient ingredient = new Ingredient();
                    List<IngredientPrice> prices = subjectPrice.findByIdIngredient(res.getLong("id"));
                    List<StockMovement> stockMovements = subjectStockMovement.findByIngredient(res.getLong("id"));
                    ingredient.setId(res.getLong("id"));
                    ingredient.setName(res.getString("name"));
                    ingredient.setDateTime(res.getTimestamp("dateTime").toInstant());
                    ingredient.setPrices(prices);
                    ingredient.setUnit(Unit.valueOf(res.getString("unit")));
                    ingredient.setStockMovements(stockMovements);

                    ingredients.add(ingredient);
                }
                return ingredients;
            }
        } catch (SQLException e){
            throw new RuntimeException("Not implemented" , e);
        }
    }

    public int getIngredientCost(long idDish) {
        int totalPriceDish = 0;
        String sql = "SELECT SUM(i.price * di.required_quantity) as total FROM dish d " +
                "INNER JOIN dish_ingredient di ON d.id = di.dish_id inner join ingredient i on di.ingredient_id=i.id where di.dish_id=? GROUP BY d.id";
        try (Connection connection = db.getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)){
            pstm.setInt(1,(int) idDish);
            try (ResultSet res = pstm.executeQuery()){
                while (res.next()){
                    totalPriceDish = res.getInt("total");
                }
            }
        } catch (SQLException e){
            throw new RuntimeException("Not implemented" , e);
        }
        return totalPriceDish;
    }


}
