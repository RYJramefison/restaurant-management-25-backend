package school.hei.pingpongspring.repository.dao;

import org.springframework.stereotype.Repository;
import school.hei.pingpongspring.entity.Ingredient;
import school.hei.pingpongspring.entity.Unit;
import school.hei.pingpongspring.repository.bd.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static school.hei.pingpongspring.repository.dao.CriteriaAccepted.*;

@Repository
public class IngredientDAO implements CrudDAO<Ingredient> {
    DataSource db = new DataSource();

    public List<Ingredient> findIngredientByCriteria(List<Criteria> criteria, int size, int page){
        List<Ingredient> ingredients = new ArrayList<>();
        String sql = "SELECT id, name, datetime, price, unit FROM ingredient WHERE 1=1";
        int offset = size * (page - 1);
        String orderBySql = " ORDER BY ";
        String paginationSql = " limit "+ size + " offset " + offset;

        for (Criteria c : criteria) {
            if (name.equals(c.getColumn())){
                sql += " and name like '%"+ c.getValue().toString() + "%'";
            }
            else if (dateTimeLow.equals(c.getColumn())){
                sql += " and datetime >='"+ c.getValue().toString() + "'";
            }
            else if (dateTimeHigh.equals(c.getColumn())){
                sql += " and datetime <='"+ c.getValue().toString() + "'";
            }
            else if (unit.equals(c.getColumn())){
                sql += " and unit ='"+ c.getValue().toString() + "'";
            }
            else if (priceLow.equals(c.getColumn())){
                sql += " and price >=" + c.getValue();
            }
            else if (priceHigh.equals(c.getColumn())){
                sql += " and price <=" + c.getValue();
            }
            else if(nameOrder.equals(c.getColumn())){
                if (orderBySql.length() == 10){
                    orderBySql += " name " + c.getValue().toString();
                }
                else {
                    orderBySql += ", name " + c.getValue().toString();
                }
            }
            else if(dateTimeOrder.equals(c.getColumn())){
                if (orderBySql.length() == 10){
                    orderBySql += " datetime " + c.getValue().toString();
                }
                else {
                    orderBySql += ", datetime " + c.getValue().toString();
                }
            }
            else if(priceOrder.equals(c.getColumn())){
                if (orderBySql.length() == 10){
                    orderBySql += " price " + c.getValue().toString();
                }
                else {
                    orderBySql += ", price " + c.getValue().toString();
                }
            }
            else if(unitOrder.equals(c.getColumn())){
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
                    Ingredient ingredient = new Ingredient();
                    ingredient.setId(res.getInt("id"));
                    ingredient.setName(res.getString("name"));
                    ingredient.setDateTime(res.getTimestamp("dateTime").toInstant());
                    ingredient.setPrice(res.getInt("price"));
                    ingredient.setUnit(Unit.valueOf(res.getString("unit")));
                    ingredients.add(ingredient);
                }
                return ingredients;
        } catch (SQLException e){
            throw new RuntimeException("Not implemented" , e);
        }
    }

    @Override
    public Ingredient findById(long id) {
        Ingredient ingredient = new Ingredient();

        String sql = "SELECT id, name, datetime, price, unit FROM ingredient WHERE id=?";

        try (Connection connection = db.getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)){
            pstm.setInt(1,(int) id);

            try (ResultSet res = pstm.executeQuery()){

                while (res.next()){

                    ingredient.setId(res.getInt("id"));
                    ingredient.setName(res.getString("name"));
                    ingredient.setDateTime(res.getTimestamp("dateTime").toInstant());
                    ingredient.setPrice(res.getInt("price"));
                    ingredient.setUnit(Unit.valueOf(res.getString("unit")));
                }
            }
            return ingredient;
        } catch (SQLException e){
            throw new RuntimeException("Not implemented", e);
        }
    }

    @Override
    public void save(Ingredient toSave) {
        String sql = "INSERT INTO ingredient (id,name,datetime,price,unit) VALUES (?,?,?,?,?)";

        try (Connection connection = db.getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)){
                pstm.setInt(1,(int) toSave.getId());
                pstm.setString(2,toSave.getName());
                pstm.setTimestamp(3,Timestamp.valueOf(String.valueOf(toSave.getDateTime())));
                pstm.setDouble(4,toSave.getPrice());
                pstm.setString(5, toSave.getUnit().name());

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
            pstm.setDouble(3,toUpdate.getPrice());
            pstm.setString(4,toUpdate.getUnit().name());
            pstm.setInt(5,id);

            pstm.executeUpdate();

        } catch (SQLException e){
            throw new RuntimeException("Not implemented", e);
        }
    }
}
