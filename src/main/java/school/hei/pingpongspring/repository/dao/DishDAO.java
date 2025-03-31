package school.hei.pingpongspring.repository.dao;


import org.springframework.stereotype.Repository;
import school.hei.pingpongspring.entity.Dish;
import school.hei.pingpongspring.entity.DishIngredient;
import school.hei.pingpongspring.entity.Ingredient;
import school.hei.pingpongspring.entity.Unit;
import school.hei.pingpongspring.repository.bd.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DishDAO implements CrudDAO<Dish> {
    DataSource db = new DataSource();
    IngredientDAO ingredientCrud = new IngredientDAO();
    Dish_IngredientDAO dish_ingredientCrud = new Dish_IngredientDAO();

    @Override
    public Dish findById(long id) {
        String sql = "select id, name, price from dish where id=?";

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
        String sql = "INSERT INTO dish (id, name, price) VALUES (?,?,?)";
        String updateSql = "UPDATE dish SET name=?, price=? WHERE id=?";
        boolean exist= false;
        try (Connection connection = db.getConnection()){
            if (exist){
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
                        if (ingredient.getUnit() == Unit.G) {
                            requiredQuantity = (float) ingredient.getPrice() / 20;
                        }
                        if (ingredient.getUnit() == Unit.L) {
                            requiredQuantity = (float) ingredient.getPrice() / 10000;
                        }
                        if (ingredient.getUnit() == Unit.U) {
                            requiredQuantity = (float) ingredient.getPrice() / 1000;
                        }
                        Unit unit = ingredient.getUnit();
                        dishIngredient.setDishId(dishId);
                        dishIngredient.setIngredientId(ingredientId);
                        dishIngredient.setRequiredQuantity(requiredQuantity);
                        dishIngredient.setUnit(unit);
                        dish_ingredientCrud.save(dishIngredient);
                    }
                }
            }
            else {
                try (PreparedStatement pstm = connection.prepareStatement(updateSql)){
                pstm.setString(1, toSave.getName());
                pstm.setInt(2, toSave.getPrice());
                    pstm.setInt(3, (int) toSave.getId());


                pstm.executeUpdate();

                toSave.getIngredients().forEach(ingredient -> ingredientCrud.update((int) ingredient.getId(), ingredient));

                    for (Ingredient ingredient : toSave.getIngredients()) {
                        DishIngredient dishIngredient = new DishIngredient();
                        long idDish = toSave.getId();
                        long idIngredient = ingredient.getId();
                        float requiredQuantity = 0;
                        if (ingredient.getUnit() == Unit.G) {
                            requiredQuantity = (float) ingredient.getPrice() / 20;
                        }
                        if (ingredient.getUnit() == Unit.L) {
                            requiredQuantity = (float) ingredient.getPrice() / 10000;
                        }
                        if (ingredient.getUnit() == Unit.U) {
                            requiredQuantity = (float) ingredient.getPrice() / 1000;
                        }
                        Unit unit = ingredient.getUnit();
                        dishIngredient.setDishId(idDish);
                        dishIngredient.setIngredientId(idIngredient);
                        dishIngredient.setRequiredQuantity(requiredQuantity);
                        dishIngredient.setUnit(unit);
                        dish_ingredientCrud.update((int) toSave.getId(),(int) ingredient.getId(),dishIngredient);
                    }
                }
            }

        } catch (SQLException e){
            throw new RuntimeException("Not implemented", e);
        }
    }



    public List<Ingredient> findIngredientByDish(long idDish){
        String sql = "SELECT i.id, i.name, i.datetime, (i.price * di.required_quantity) as price, di.unit\n" +
                "FROM ingredient i INNER JOIN dish_ingredient di ON i.id = di.ingredient_id WHERE di.dish_id=?";

        try (Connection connection = db.getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)){
            pstm.setInt(1, (int) idDish);

            try (ResultSet res = pstm.executeQuery()){
                List<Ingredient> ingredients = new ArrayList<>();
                while (res.next()){
                    Ingredient ingredient = new Ingredient();
                    ingredient.setId(res.getInt("id"));
                    ingredient.setName(res.getString("name"));
                    ingredient.setDateTime(res.getTimestamp("dateTime").toInstant());
                    ingredient.setPrice(res.getInt("price"));
                    ingredient.setUnit(Unit.valueOf(res.getString("unit")));

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
