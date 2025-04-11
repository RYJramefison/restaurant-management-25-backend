package school.hei.pingpongspring.repository.dao;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import school.hei.pingpongspring.model.Dish;
import school.hei.pingpongspring.model.DishOrder;
import school.hei.pingpongspring.model.DishOrderStatus;
import school.hei.pingpongspring.model.StatusOrder;
import school.hei.pingpongspring.repository.bd.DataSource;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DishOrderDAO implements CrudDAO<DishOrder>{
    private final DataSource dataSource ;
    private final DishDAO subjectDish ;


    public List<DishOrder> getAll(int page, int size) {
        List<DishOrder> dishOrders = new ArrayList<>();
        String sql = "select id, dish_id,order_id, quantity from dish_order limit ? offset ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)) {
            int offset = (page - 1) * size;
            pstm.setInt(1, size);
            pstm.setInt(2, offset);
            try (ResultSet res = pstm.executeQuery()) {
                while (res.next()){
                    DishOrder dishOrder = new DishOrder();
                    dishOrder.setId(res.getLong("id"));

                    Dish dishOfOrder = subjectDish.findById(res.getLong("dish_id"));
                    dishOrder.setDish(dishOfOrder);

                    dishOrder.setOrderId(res.getInt("order_id"));

                    dishOrder.setQuantity(res.getInt("quantity"));

                    dishOrder.setStatus(getStatusByDishOrder(res.getLong("id")));

                    dishOrders.add(dishOrder);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("erreur sur la recuperation du dishOrder ",e);
        }
        return dishOrders;
    }


    public List<DishOrderStatus> getStatusByDishOrder(long id){
        List<DishOrderStatus> dishOrderStatus = new ArrayList<>();
        String sql = "SELECT id, date, status, dish_order_id FROM dish_order_status WHERE dish_order_id=?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)){
            pstm.setFloat(1, id);
            try (ResultSet res = pstm.executeQuery()){
                while(res.next()){
                    DishOrderStatus status = new DishOrderStatus();
                    status.setId(res.getLong("id"));
                    status.setDateTime(res.getTimestamp("date").toInstant());
                    status.setStatusOrder(StatusOrder.valueOf(res.getString("status")));
                    status.setDishOrderId(res.getLong("dish_order_id"));
                    dishOrderStatus.add(status);
                }
            }
        } catch (SQLException e){
            throw new RuntimeException("erreur sur la recuperation du status ",e);
        }
        return dishOrderStatus;
    }

    @Override
    public DishOrder findById(long id) {
        DishOrder dishOrder = new DishOrder();
        String sql = "select id, dish_id, order_id, quantity from dish_order where id=?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setLong(1, id);
            try (ResultSet res = pstm.executeQuery()) {
                while (res.next()){
                    dishOrder.setId(res.getLong("id"));

                    Dish dishOfOrder = subjectDish.findById(res.getLong("dish_id"));
                    dishOrder.setDish(dishOfOrder);

                    dishOrder.setOrderId(res.getLong("order_id"));

                    dishOrder.setQuantity(res.getInt("quantity"));

                    dishOrder.setStatus(getStatusByDishOrder(id));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("erreur sur la recuperation du dishOrder ",e);
        }
        return dishOrder;
    }

    public List<DishOrder> findSalesDishOrder(int X, Instant dateFirst, Instant dateLast) {
        DishOrder dishOrder = new DishOrder();
        String sql = "select id, dish_id, order_id, quantity from dish_order dio inner join dish_order_status dios\n" +
                "    on dio.id = dios.dish_order_id where dios.status='CONFIRMED' and dios.date>=? and dios.date <=? LIMIT ? ";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setTimestamp(1, Timestamp.from(dateFirst));
            pstm.setTimestamp(2, Timestamp.from(dateLast));
            pstm.setInt(3, X);
            try (ResultSet res = pstm.executeQuery()) {
                while (res.next()){
                    dishOrder.setId(res.getLong("id"));

                    Dish dishOfOrder = subjectDish.findById(res.getLong("dish_id"));
                    dishOrder.setDish(dishOfOrder);

                    dishOrder.setOrderId(res.getLong("order_id"));

                    dishOrder.setQuantity(res.getInt("quantity"));

                    dishOrder.setStatus(getStatusByDishOrder(id));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("erreur sur la recuperation du dishOrder ",e);
        }
        return dishOrder;
    }

    public List<DishOrder> findDishOrderByDish(long dishId){
        List<DishOrder> dishOrders = new ArrayList<>();
        String sql ="select * from dish_order where dish_id=?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setInt(1,(int) dishId);
            try (ResultSet res = pstm.executeQuery()) {
                while (res.next()){
                    DishOrder dishOrder = new DishOrder();
                    dishOrder.setId(res.getLong("id"));

                    Dish dishOfOrder = subjectDish.findById(res.getLong("dish_id"));
                    dishOrder.setDish(dishOfOrder);

                    dishOrder.setOrderId(res.getInt("order_id"));

                    dishOrder.setQuantity(res.getInt("quantity"));

                    dishOrder.setStatus(getStatusByDishOrder(res.getLong("id")));

                    dishOrders.add(dishOrder);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("erreur sur la recuperation du dishOrder ",e);
        }
        return dishOrders;
    }



    public void saveStatus(DishOrderStatus status){
        String sql = "INSERT INTO dish_order_status ( date, status, dish_order_id) VALUES (?,?::status_order,?)";

        try (Connection connection = dataSource.getConnection();
            PreparedStatement pstm = connection.prepareStatement(sql)){

            pstm.setTimestamp(1, Timestamp.from(status.getDateTime()));
            pstm.setString(2, String.valueOf(status.getStatusOrder()));
            pstm.setLong(3, status.getDishOrderId());

            pstm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("erreur sur l'ajout du status ",e);
        }
    }

    public List<DishOrder> saveAll(List<DishOrder> entities) throws SQLException {
        List<DishOrder> dishOrders = new ArrayList<>();
        String sql = "INSERT INTO dish_order (id, dish_id, order_id, quantity) VALUES (?,?,?,?)";

        try (Connection connection = dataSource.getConnection();
            PreparedStatement pstm = connection.prepareStatement(sql)){
            entities.forEach( entity -> {
                try {
                    pstm.setLong(1, entity.getId());
                    pstm.setLong(2, entity.getDish().getId());
                    pstm.setLong(3, entity.getOrderId());
                    pstm.setInt(4, entity.getQuantity());
                    pstm.executeUpdate();

                    entity.getStatus().forEach(status -> {
                        saveStatus(status);
                    });

                    dishOrders.add(entity);
                } catch (SQLException e) {
                    throw new RuntimeException("erreur sur l'ajout du dishOrder ",e);
                }
            });
        }
        return dishOrders;
    }




    @Override
    public void save(DishOrder toSave) {

    }
}
