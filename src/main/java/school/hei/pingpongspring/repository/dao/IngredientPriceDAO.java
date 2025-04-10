package school.hei.pingpongspring.repository.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import school.hei.pingpongspring.model.IngredientPrice;
import school.hei.pingpongspring.repository.bd.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class IngredientPriceDAO implements CrudDAO<IngredientPrice>{
    private final DataSource dataSource;



    @Override
    public IngredientPrice findById(long id) {
        return null;
    }

    @Override
    public void save(IngredientPrice toSave) {

    }

    public List<IngredientPrice> saveAll(List<IngredientPrice> entities) {
        List<IngredientPrice> prices = new ArrayList<>();
        String sql = "INSERT INTO ingredient_price (ingredient_id, price, date) " +
                "VALUES (?, ?, ?) " +
                "ON CONFLICT (id) DO NOTHING " +
                "RETURNING id, ingredient_id, price, date";

        try (Connection connection = dataSource.getConnection()) {
            for (IngredientPrice entityToSave : entities) {
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setLong(1, entityToSave.getIngredientId());
                    statement.setFloat(2, (float) entityToSave.getPrice());
                    statement.setTimestamp(3, Timestamp.from(entityToSave.getDate()));
                    try (ResultSet resultSet = statement.executeQuery()) {
                        if (resultSet.next()) {
                            prices.add(mapFromResultSet(resultSet));
                        }
                    }
                }
            }
            return prices;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving ingredient prices: " + e.getMessage(), e);
        }
    }


    public List<IngredientPrice> findByIdIngredient(Long idIngredient) {
        List<IngredientPrice> prices = new ArrayList<>();
        String sql = "select p.id, p.ingredient_id, p.price, p.date from ingredient_price p"
                + " join ingredient i on p.ingredient_id = i.id"
                + " where i.id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, idIngredient);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    IngredientPrice price = mapFromResultSet(resultSet);
                    prices.add(price);
                }
                return prices;
            }
        } catch (SQLException e) {
            throw new RuntimeException("erreur list prices ingredient "+e);
        }
    }

    private IngredientPrice mapFromResultSet(ResultSet resultSet) throws SQLException {
        IngredientPrice price = new IngredientPrice();
        price.setId(resultSet.getLong("id"));
        price.setIngredientId(resultSet.getLong("ingredient_id"));
        price.setPrice(resultSet.getDouble("price"));
        price.setDate(resultSet.getTimestamp("date").toInstant());
        return price;
    }
}
