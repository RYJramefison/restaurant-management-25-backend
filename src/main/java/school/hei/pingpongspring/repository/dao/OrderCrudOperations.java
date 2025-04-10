package school.hei.pingpongspring.repository.dao;



import school.hei.pingpongspring.model.DishOrder;
import school.hei.pingpongspring.model.Order;
import school.hei.pingpongspring.model.OrderStatus;
import school.hei.pingpongspring.model.StatusOrder;
import school.hei.pingpongspring.repository.bd.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderCrudOperations implements CrudDAO<Order>{
    DataSource dataSource = new DataSource();
    DishOrderCrudOperations subjectDishOrder = new DishOrderCrudOperations();

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

    public Order findById(Long id) {
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
                    DishOrder dishOrder = subjectDishOrder.findById(res.getLong("id"));
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

                    List<DishOrder> dishOrders = entity.getDishOrders();
                    subjectDishOrder.saveAll(dishOrders);

                    entity.getStatus().forEach(status -> {
                        saveStatus(status);
                    });

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

    @Override
    public Order findById(long id) {
        return null;
    }

    @Override
    public void save(Order toSave) {

    }
}
