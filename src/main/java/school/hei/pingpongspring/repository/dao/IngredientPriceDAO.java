package school.hei.pingpongspring.repository.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import school.hei.pingpongspring.entity.IngredientPrice;
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
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement("insert into price (id, ingredient_id, price, date) values (?, ?, ?, ?)"
                             + " on conflict (id) do nothing"
                             + " returning id, ingredient_id, price, date");) {
            entities.forEach(entityToSave -> {
                try {
                    statement.setLong(1, entityToSave.getId());
                    statement.setLong(2, entityToSave.getIngredientId());
                    statement.setFloat(3, (float) entityToSave.getPrice());
                    statement.setTimestamp(4, Timestamp.from(entityToSave.getDate()));
                    statement.addBatch(); // group by batch so executed as one query in database
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    prices.add(mapFromResultSet(resultSet));
                }
            }
            return prices;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<IngredientPrice> findByIdIngredient(Long idIngredient) {
        List<IngredientPrice> prices = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("select p.id, p.ingredient_id, p.price, p.date from ingredient_price p"
                     + " join ingredient i on p.ingredient_id = i.id"
                     + " where i.id = ?")) {
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
        price.setPrice(resultSet.getDouble("price"));
        price.setDate(resultSet.getTimestamp("date").toInstant());
        return price;
    }
}
