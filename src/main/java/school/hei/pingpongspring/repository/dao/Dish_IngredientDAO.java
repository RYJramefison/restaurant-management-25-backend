package school.hei.pingpongspring.repository.dao;



import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import school.hei.pingpongspring.mapper.DishIngredientMapper;
import school.hei.pingpongspring.model.DishIngredient;
import school.hei.pingpongspring.model.Ingredient;
import school.hei.pingpongspring.model.Unit;
import school.hei.pingpongspring.repository.bd.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class Dish_IngredientDAO {
    private final DataSource db;
    private final DishIngredientMapper dishIngredientMapper;

    public DishIngredient findById(int idDIsh, int idIngredient) {
        String sql = "SELECT dish_id, ingredient_id, required_quantity,unit FROM dish_ingredient WHERE dish_id=? AND ingredient_id=?";
        try (Connection connection = db.getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)){
            pstm.setInt(1,idDIsh);
            pstm.setInt(2,idIngredient);

            try (ResultSet res = pstm.executeQuery()){
                DishIngredient dishIngredient = new DishIngredient();
                while(res.next()){
                    dishIngredient.setDishId(res.getInt("dish_id"));
                    dishIngredient.setIngredientId(res.getInt("ingredient_id"));
                    dishIngredient.setRequiredQuantity(Double.valueOf(res.getFloat("required_quantity")));
                    dishIngredient.setUnit(Unit.valueOf(res.getString("unit")));
                }
                return dishIngredient;
            }
        } catch (SQLException e){
            throw new RuntimeException("Not implemented", e);
        }
    }

    public void save(DishIngredient toSave) {
        DishIngredient dishIngredient = new DishIngredient();
        String sql = "INSERT INTO dish_ingredient (dish_id, ingredient_id, required_quantity, unit) VALUES (?,?,?,?::unit)" +
                " on conflict (dish_id,ingredient_id) do update set required_quantity=excluded.required_quantity, unit=excluded.unit";

        try (Connection connection = db.getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)){
            pstm.setInt(1, (int) toSave.getDishId());
            pstm.setInt(2, (int) toSave.getIngredientId());
            pstm.setFloat(3, (float) toSave.getRequiredQuantity());
            pstm.setString(4, String.valueOf(toSave.getUnit()));

            pstm.executeUpdate();
        }   catch (SQLException e) {
            throw new RuntimeException("Not implemented", e);
        }
    }

    public List<DishIngredient> saveAll(List<DishIngredient> toSaves) {
        List<DishIngredient> dishIngredients = new ArrayList<>();
        DishIngredient dishIngredient = new DishIngredient();
        String sql = "INSERT INTO dish_ingredient (dish_id, ingredient_id, required_quantity, unit) VALUES (?,?,?,?::unit)" +
                " on conflict (id) do update set required_quantity=excluded.required_quantity, unit=excluded.unit";

        try (Connection connection = db.getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)){
            for (DishIngredient toSave : toSaves) {
                pstm.setInt(1, (int) toSave.getDishId());
                pstm.setInt(2, (int) toSave.getIngredientId());
                pstm.setFloat(3, (float) toSave.getRequiredQuantity());
                pstm.setString(4, String.valueOf(toSave.getUnit()));

                try (ResultSet resultSet = pstm.executeQuery()) {
                    if (resultSet.next()) {
                        DishIngredient saved = dishIngredientMapper.apply(resultSet);
//                        entityToSave.getPrices().forEach(p -> p.setIngredientId(saved.getId()));
//                        entityToSave.getStockMovements().forEach(m -> m.setIngredientId(saved.getId()));
//
//                        subjectPrice.saveAll(entityToSave.getPrices());
//                        stockMovementDAO.saveAll(entityToSave.getStockMovements());

                        dishIngredients.add(saved);
                    }
                }
            }
        return dishIngredients;
        }   catch (SQLException e) {
            throw new RuntimeException("Not implemented", e);
        }
    }

    public void update(int dishId, int ingredientId, DishIngredient toUpdate) {
        String sql = "UPDATE dish_ingredient SET required_quantity=?, unit? WHERE dish_id=? AND ingredient_id=?";
        try (Connection connection = db.getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)){

            pstm.setFloat(1, (float) toUpdate.getRequiredQuantity());
            pstm.setString(2, String.valueOf(toUpdate.getUnit()));
            pstm.setInt(3, dishId);
            pstm.setInt(4, ingredientId);

            pstm.executeUpdate();
        }   catch (SQLException e) {
            throw new RuntimeException("Not implemented", e);
        }
    }
}
