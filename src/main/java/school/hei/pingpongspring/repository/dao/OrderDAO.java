package school.hei.pingpongspring.repository.dao;



import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import school.hei.pingpongspring.controller.rest.SalesRest;
import school.hei.pingpongspring.model.*;
import school.hei.pingpongspring.repository.bd.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderDAO implements CrudDAO<Order>{
    private final DataSource dataSource ;
    private final DishOrderDAO subjectDishOrder ;
    private final DishDAO subjectDish;

    public List<SalesRest> getSales() {
        List<SalesRest> salesRestList = new ArrayList<>();
        String sql = "SELECT\n" +
                "                   d.id,\n" +
                "                   d.name, d.price,\n" +
                "                  SUM(do2.quantity) as total_quantity\n" +
                "                FROM \"order\" o\n" +
                "         INNER JOIN order_status os ON o.id = os.order_id\n" +
                "                        INNER JOIN dish_order do2 ON o.id = do2.order_id\n" +
                "                    INNER JOIN dish d ON do2.dish_id = d.id\n" +
                "                WHERE os.status = 'CONFIRMED'\n" +
                "                GROUP BY d.id, d.name,d.price, os.status\n" +
                "                ORDER BY total_quantity DESC\n" +
                "               ";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql);
             ResultSet res = pstm.executeQuery()) {

            while (res.next()) {
                SalesRest salesRest = new SalesRest();
                salesRest.setDishId(res.getLong("id"));
                salesRest.setDishName(res.getString("name"));
                salesRest.setQuantitySold(res.getInt("total_quantity"));
                salesRest.setPrice(res.getInt("price"));


                salesRestList.add(salesRest);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des commandes livrées", e);
        }
        return salesRestList;
    }


    public List<Order> getAll(int page, int size) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT id, reference FROM \"order\" LIMIT ? OFFSET ?";
        try (Connection connection = dataSource.getConnection();
            PreparedStatement pstm = connection.prepareStatement(sql)){
            int offset = (page - 1) * size;
            pstm.setInt(1, size);
            pstm.setInt(2, offset);
            try (ResultSet res = pstm.executeQuery()){
                while (res.next()){
                    Order order = new Order();
                    order.setId(res.getLong("id"));
                    order.setReference(res.getString("reference"));

                    List<OrderStatus> statuses = getStatusByOrder(res.getLong("id"));
                    order.setStatus(statuses);

                    List<DishOrder> dishOrders = getDishByOrder(res.getLong("id"));
                    order.setDishOrders(dishOrders);

                    orders.add(order);
                }
            }
        } catch (SQLException e){
            throw new RuntimeException("Get all order not implemented ",e);
        }
        return orders;
    }

    @Override
    public Order findById(long id) {
        Order order = new Order();
        String sql = "SELECT * FROM \"order\" WHERE id=?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)){
            pstm.setLong(1, id);
            try (ResultSet res = pstm.executeQuery()){
                while (res.next()){
                    order.setId(res.getLong("id"));
                    order.setReference(res.getString("reference"));

                    List<DishOrder> dishOrders = getDishByOrder(res.getLong("id"));
                    order.setDishOrders(dishOrders);

                    List<OrderStatus> statuses = getStatusByOrder(res.getLong("id"));
                    order.setStatus(statuses);
                }
            }
        } catch (SQLException e){
            throw new RuntimeException("Find order by id not implemented ",e);
        }
        return order;
    }

    @Override
    public void save(Order toSave) {

    }

    public List<Order> findSaleOrder(int X) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT o.id, o.reference FROM \"order\" o INNER JOIN order_status os ON o.id = os.order_id\n" +
                "WHERE os.status = 'FINISHED' LIMIT ?;\n";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)){
            pstm.setInt(1, X);
            try (ResultSet res = pstm.executeQuery()){
                while (res.next()){
                    Order order = new Order();
                    order.setId(res.getLong("id"));
                    order.setReference(res.getString("reference"));

                    List<OrderStatus> statuses = getStatusByOrder(res.getLong("id"));
                    order.setStatus(statuses);

                    List<DishOrder> dishOrders = getDishByOrder(res.getLong("id"));
                    order.setDishOrders(dishOrders);

                    orders.add(order);
                }
            }
        } catch (SQLException e){
            throw new RuntimeException("Get all order not implemented ",e);
        }
        return orders;
    }

    public Order findByReference(String reference) {
        Order order = new Order();
        String sql = "SELECT * FROM \"order\" WHERE reference=?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)){
            pstm.setString(1, reference);
            try (ResultSet res = pstm.executeQuery()){
                while (res.next()){
                    order.setId(res.getLong("id"));
                    order.setReference(res.getString("reference"));

                    List<DishOrder> dishOrders = getDishByOrder(res.getLong("id"));
                    order.setDishOrders(dishOrders);

                    List<OrderStatus> statuses = getStatusByOrder(res.getLong("id"));
                    order.setStatus(statuses);
                }
            }
        } catch (SQLException e){
            throw new RuntimeException("Find order by id not implemented ",e);
        }
        return order;
    }

    public List<OrderStatus> getStatusByOrder(long id){
        List<OrderStatus> orderStatuses = new ArrayList<>();
        String sql = "SELECT id, date, status, order_id FROM order_status WHERE order_id=?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)){
            pstm.setFloat(1, id);

            try (ResultSet res = pstm.executeQuery()){
                while(res.next()){
                    OrderStatus status = new OrderStatus();
                    status.setId(res.getLong("id"));
                    status.setDateTime(res.getTimestamp("date").toInstant());
                    status.setStatusOrder(StatusOrder.valueOf(res.getString("status")));
                    status.setOrderId(res.getLong("order_id"));
                    orderStatuses.add(status);
                }
            }
        } catch (SQLException e){
            throw new RuntimeException("erreur sur la recuperation du status ",e);
        }
        return orderStatuses;
    }

    public List<DishOrder> getDishByOrder(long id){
        List<DishOrder> dishOrders = new ArrayList<>();
        String sql = "select id, dish_id, order_id, quantity from dish_order where order_id=?";
        try (Connection connection = dataSource.getConnection();
            PreparedStatement pstm = connection.prepareStatement(sql)){
            pstm.setLong(1,id);
            try (ResultSet res = pstm.executeQuery()){
                while (res.next()){
                    DishOrder dishOrder =new DishOrder();
                    dishOrder.setId(res.getLong("id"));
                    Dish dish = subjectDish.findById(res.getLong("dish_id"));
                    dishOrder.setDish(dish);
                    dishOrder.setOrderId(res.getLong("order_id"));
                    dishOrder.setQuantity(res.getInt("quantity"));
                    List<DishOrderStatus> dishOrderStatus =  subjectDishOrder.getStatusByDishOrder(res.getLong("id"));
                    dishOrder.setStatus(dishOrderStatus);
                    dishOrders.add(dishOrder);
                }
            }
        } catch (SQLException e){
            throw new RuntimeException("Get dishOrder by order not implemented ",e);
        }
        return dishOrders;
    }

    public void saveStatus(OrderStatus status){
        String sql = "INSERT INTO order_status (id, date, status, order_id) VALUES (?,?,?::status_order,?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)){

            pstm.setLong(1,status.getId());
            pstm.setTimestamp(2, Timestamp.from(status.getDateTime()));
            pstm.setString(3, String.valueOf(status.getStatusOrder()));
            pstm.setLong(4, status.getOrderId());

            pstm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Order> saveAll(List<Order> entities) throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "INSERT INTO \"order\" (id, reference) VALUES (?,?)";
        try (Connection connection = dataSource.getConnection();
            PreparedStatement pstm = connection.prepareStatement(sql)){
            entities.forEach(entity -> {
                try {
                    pstm.setLong(1, entity.getId());
                    pstm.setString(2,entity.getReference());
                    pstm.executeUpdate();

                    if (entity.getStatus() != null){
                        entity.getStatus().forEach(status -> {
                            saveStatus(status);
                        });
                    }
                    if (entity.getDishOrders() != null){
                        List<DishOrder> dishOrders = entity.getDishOrders();
                        subjectDishOrder.saveAll(dishOrders);
                    }


                    orders.add(entity);

                } catch (SQLException e){
                    throw new RuntimeException("Save order failed ", e);
                }
            });
        }
        return orders;
    }

    public void update(long id, Order toUpdate){
        String sql = "UPDATE \"order\" SET id=?, reference=? WHERE id=?";
        Order order = findById(id);
            if (order.getStatus().size() <= 1){
                try (Connection connection = dataSource.getConnection();
                     PreparedStatement pstm = connection.prepareStatement(sql)){

                    pstm.setLong(1, toUpdate.getId());
                    pstm.setString(2,toUpdate.getReference());

                    List<DishOrder> dishOrders = toUpdate.getDishOrders();
                    subjectDishOrder.saveAll(dishOrders);

                    toUpdate.getStatus().forEach(status -> saveStatus(status));

                    pstm.executeUpdate();

                } catch (SQLException e){
                    throw new RuntimeException("Save order failed ", e);
                };
            }
            else {
                throw new RuntimeException("You can't update");
            }

        }




    public Order saveOrder(Order toSave) throws SQLException {



        String sql = "INSERT INTO \"order\" (reference) VALUES (?) RETURNING id, reference";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstm.setString(1, toSave.getReference());
            int affectedRows = pstm.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating order failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pstm.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    toSave.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating order failed, no ID obtained.");
                }
            }

            return toSave;

        } catch (SQLException e) {
            throw new SQLException("Failed to save order: " + e.getMessage(), e);
        }
    }
}
