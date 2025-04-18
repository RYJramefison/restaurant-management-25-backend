package school.hei.pingpongspring.repository.dao;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;
import school.hei.pingpongspring.mapper.IngredientMapper;
import school.hei.pingpongspring.model.Ingredient;
import school.hei.pingpongspring.model.IngredientPrice;
import school.hei.pingpongspring.model.Unit;
import school.hei.pingpongspring.repository.bd.DataSource;
import school.hei.pingpongspring.service.exception.ServerException;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


@Repository
@RequiredArgsConstructor
public class IngredientDAO implements CrudDAO<Ingredient> {
    private final DataSource db;
    private final IngredientPriceDAO subjectPrice;
    private final StockMovementDAO stockMovementDAO;
    private final IngredientMapper ingredientMapper;
    private final DataSource dataSource;


    public List<Ingredient> findIngredientByCriteria(List<Criteria> criteria, int size, int page){
        List<Ingredient> ingredients = new ArrayList<>();
        String sql = "SELECT id, name, datetime, unit FROM ingredient WHERE 1=1";
        int offset = size * (page - 1);
        String orderBySql = " ORDER BY ";
        String paginationSql = " limit "+ size + " offset " + offset;

        for (Criteria c : criteria) {
            if ("name".equals(c.getColumn())){
                sql += " and name ilike '%"+ c.getValue().toString() + "%'";
            }
            else if ("dateTimeLow".equals(c.getColumn())){
                sql += " and datetime >='"+ c.getValue().toString() + "'";
            }
            else if ("dateTimeHigh".equals(c.getColumn())){
                sql += " and datetime <='"+ c.getValue().toString() + "'";
            }
            else if ("unit".equals(c.getColumn())){
                sql += " and unit ='"+ c.getValue().toString() + "'";
            }
//            else if (priceLow.equals(c.getColumn())){
//                sql += " and price >=" + c.getValue();
//            }
//            else if (priceHigh.equals(c.getColumn())){
//                sql += " and price <=" + c.getValue();
//            }
            else if("nameOrder".equals(c.getColumn())){
                if (orderBySql.length() == 10){
                    orderBySql += " name " + c.getValue().toString();
                }
                else {
                    orderBySql += ", name " + c.getValue().toString();
                }
            }
            else if("dateTimeOrder".equals(c.getColumn())){
                if (orderBySql.length() == 10){
                    orderBySql += " datetime " + c.getValue().toString();
                }
                else {
                    orderBySql += ", datetime " + c.getValue().toString();
                }
            }
//            else if("priceOrder".equals(c.getColumn())){
//                if (orderBySql.length() == 10){
//                    orderBySql += " price " + c.getValue().toString();
//                }
//                else {
//                    orderBySql += ", price " + c.getValue().toString();
//                }
//            }
            else if("unitOrder".equals(c.getColumn())){
                if (orderBySql.length() == 10){
                    orderBySql += " unit " + c.getValue().toString();
                }
                else {
                    orderBySql += ", unit " + c.getValue().toString();
                }
            }
        }

        if (orderBySql.length() == 10){
            sql += paginationSql;
        }
        if (orderBySql.length() > 10 ){
            sql += orderBySql + paginationSql;
        }

        try (Connection connection = db.getConnection();
             Statement stm = connection.createStatement();
             ResultSet res = stm.executeQuery(sql)){
                while (res.next()){
                    Ingredient ingredient = ingredientMapper.apply(res);
                    ingredients.add(ingredient);
                }
                return ingredients;
        } catch (SQLException e){
            throw new RuntimeException("Not implemented" , e);
        }
    }

    @Override
    public Ingredient findById(long id) {
        String sql = "SELECT id, name, datetime, unit FROM ingredient WHERE id=?";

        try (Connection connection = db.getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)){
            pstm.setInt(1,(int) id);

            try (ResultSet res = pstm.executeQuery()){
                if (res.next()){
                    return ingredientMapper.apply(res);
                }
                throw new ClassNotFoundException("Ingredient.id=" + id + " not found");
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e){
            throw new RuntimeException("Not implemented", e);
        }
    }

    @Override
    public void save(Ingredient toSave) {
        String sql = "INSERT INTO ingredient (id,name,datetime,unit) VALUES (?,?,?,?)";
        List<IngredientPrice> prices = subjectPrice.saveAll(toSave.getPrices());
        try (Connection connection = db.getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)){
                pstm.setInt(1,(int) toSave.getId());
                pstm.setString(2,toSave.getName());
                pstm.setTimestamp(3,Timestamp.valueOf(String.valueOf(toSave.getDateTime())));
                pstm.setString(4, toSave.getUnit().name());

                pstm.executeUpdate();

        } catch (SQLException e){
            throw new RuntimeException("Not implemented "+ toSave, e);
        }
    }

    public void update(int id, Ingredient toUpdate) {
        String sql = "UPDATE ingredient SET name=?, datetime=?, price=?, unit=? WHERE id=?";
        try (Connection connection = db.getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)){
            pstm.setString(1,toUpdate.getName());
            pstm.setTimestamp(2,Timestamp.valueOf(String.valueOf(toUpdate.getDateTime())));
            pstm.setString(3,toUpdate.getUnit().name());
            pstm.setInt(4,id);

            pstm.executeUpdate();

        } catch (SQLException e){
            throw new RuntimeException("Not implemented", e);
        }
    }

    public List<Ingredient> saveAll(List<Ingredient> entities) {
        List<Ingredient> ingredients = new ArrayList<>();
        String sql = "INSERT INTO ingredient (id, name, datetime, unit) VALUES (?, ?, ?, ?::unit) " +
                "RETURNING id, name, datetime, unit";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            for (Ingredient entityToSave : entities) {
                statement.setLong(1, entityToSave.getId());
                statement.setString(2, entityToSave.getName());
                statement.setTimestamp(3, Timestamp.from(Instant.now()));
                statement.setString(4, entityToSave.getUnit().name());

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        Ingredient saved = ingredientMapper.apply(resultSet);
//                        entityToSave.getPrices().forEach(p -> p.setIngredientId(saved.getId()));
//                        entityToSave.getStockMovements().forEach(m -> m.setIngredientId(saved.getId()));
//
//                        subjectPrice.saveAll(entityToSave.getPrices());
//                        stockMovementDAO.saveAll(entityToSave.getStockMovements());

                        ingredients.add(saved);
                    }
                }
            }
            return ingredients;
        } catch (SQLException e) {
            throw new ServerException("Erreur lors de l'enregistrement des ingrédients "+ e);
        }
    }

    public List<Ingredient> updateAll(List<Ingredient> entities) {
        List<Ingredient> ingredients = new ArrayList<>();
        String sql = "UPDATE ingredient SET name=? WHERE id=?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            for (Ingredient entityToSave : entities) {
                statement.setString(1, entityToSave.getName());
                statement.setLong(2, entityToSave.getId());
                int affectedRows = statement.executeUpdate();

                if (affectedRows > 0) {
                    // Tu peux ici reconstruire l'ingredient si besoin, ou réutiliser entityToSave
                    Ingredient saved = entityToSave;

                    // Bien lier les prices et stockMovements à l'ingrédient
                    entityToSave.getPrices().forEach(p -> p.setIngredientId(entityToSave.getId()));
                    entityToSave.getStockMovements().forEach(m -> m.setIngredientId(entityToSave.getId()));

                    subjectPrice.saveAll(entityToSave.getPrices());
                    stockMovementDAO.saveAll(entityToSave.getStockMovements());

                    ingredients.add(saved);
                }
            }
            return ingredients;
        } catch (SQLException e) {
            throw new ServerException("Erreur lors de l'enregistrement des ingrédients : " + e);
        }
    }


}
