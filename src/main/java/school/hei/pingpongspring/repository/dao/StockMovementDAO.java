package school.hei.pingpongspring.repository.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import school.hei.pingpongspring.entity.MovementType;
import school.hei.pingpongspring.entity.StockMovement;
import school.hei.pingpongspring.entity.Unit;
import school.hei.pingpongspring.repository.bd.DataSource;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class StockMovementDAO implements CrudDAO<StockMovement> {
    private final DataSource db;


    public List<StockMovement> getAll(){
        List<StockMovement> stockMovements = new ArrayList<>();
        String sql = "SELECT id, ingredient_id, type, quantity, unit, date FROM stock_movement";

        try (Connection connection = db.getConnection();
             Statement stm = connection.createStatement();
             ResultSet res = stm.executeQuery(sql)){
                while (res.next()){

                    StockMovement stockMovement = new StockMovement();
                    stockMovement.setId(res.getInt("id"));
                    stockMovement.setIngredientId(res.getInt("ingredient_id"));
                    stockMovement.setType(MovementType.valueOf(res.getString("type")));
                    stockMovement.setQuantity(res.getFloat("quantity"));
                    stockMovement.setUnit(Unit.valueOf(res.getString("unit")));
                    stockMovement.setDate(res.getTimestamp("date").toInstant());

                    stockMovements.add(stockMovement);
                }
                return stockMovements;
        } catch (SQLException e) {
            throw new RuntimeException("Not implemented", e);
        }
    }

    @Override
    public StockMovement findById(long id) {
        return null;
    }

    @Override
    public void save(StockMovement toSave) {
        String sql = "INSERT INTO stock_movement (ingredient_id, type, quantity, unit, date) VALUES (?,?,?,?,?)";

        try (Connection connection = db.getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)){
            pstm.setInt(1,(int) toSave.getIngredientId());
            pstm.setString(2, toSave.getType().name());
            pstm.setDouble(3, toSave.getQuantity());
            pstm.setString(4, toSave.getUnit().name());
            pstm.setTimestamp(5, Timestamp.valueOf(String.valueOf(toSave.getDate())));

            pstm.executeUpdate();

        } catch (SQLException e){
            throw new RuntimeException("Not implemented", e);
        }
    }

    public List<StockMovement> findByIngredient(long id){
        List<StockMovement> stockMovements = new ArrayList<>();
        String sql = "select id, ingredient_id, type, quantity, unit,date from stock_movement where ingredient_id=?";

        try (Connection connection = db.getConnection();
            PreparedStatement pstm = connection.prepareStatement(sql)){
            pstm.setLong(1, id);

            try (ResultSet res = pstm.executeQuery()){
                while(res.next()){
                    StockMovement stockMovement = new StockMovement();
                    stockMovement.setId(res.getLong("id"));
                    stockMovement.setIngredientId(res.getLong("ingredient_id"));
                    stockMovement.setType(MovementType.valueOf(res.getString("type")));
                    stockMovement.setQuantity(Double.valueOf(res.getFloat("quantity")));
                    stockMovement.setUnit(Unit.valueOf(res.getString("unit")));
                    stockMovement.setDate(res.getTimestamp("date").toInstant());
                    stockMovements.add(stockMovement);
                }
            }
        } catch (SQLException e){
            throw new RuntimeException("get stock movement by ingredient failed "+ e);
        }
        return stockMovements;

    }

    public void update(long id, StockMovement toUpdate){
        String sql = "UPDATE stock_movement SET ingredient_id=?, unit=? WHERE id=?";

        try (Connection connection = db.getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)){
            pstm.setInt(1, (int) toUpdate.getIngredientId());
            pstm.setString(2, toUpdate.getUnit().name());
            pstm.setInt(3,(int) id);

            pstm.executeUpdate();

        } catch (SQLException e){
            throw new RuntimeException("Not implemented", e);
        }
    }
}
