package school.hei.pingpongspring.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import school.hei.pingpongspring.controller.rest.AddOrUpdateDishOrders;
import school.hei.pingpongspring.controller.rest.DishOrderRest;
import school.hei.pingpongspring.controller.rest.UpdateDishOrderStatus;
import school.hei.pingpongspring.model.DishOrder;
import school.hei.pingpongspring.model.DishOrderStatus;
import school.hei.pingpongspring.model.Order;
import school.hei.pingpongspring.repository.dao.DishDAO;
import school.hei.pingpongspring.repository.dao.DishOrderDAO;
import school.hei.pingpongspring.repository.dao.OrderDAO;

import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderDAO orderDAO;
    private final DishOrderDAO dishOrderDAO;
    private final DishDAO dishDAO;

    public Order findById(long id){
        return orderDAO.findById(id);
    }

    public List<DishOrder> getdishByOrder(long id){
        return orderDAO.getDishByOrder(id);
    }

    public List<DishOrder> updateDishOrder(long reference, List<AddOrUpdateDishOrders>  dishOrderList) throws Exception {
        List<DishOrder> dishOrders = new ArrayList<>();
        for (AddOrUpdateDishOrders addOrUpdateDishOrders : dishOrderList) {
            for (DishOrderRest dish : addOrUpdateDishOrders.getDishes()) {

                DishOrderStatus dishOrderStatus = new DishOrderStatus(Instant.now(),addOrUpdateDishOrders.getOrderStatus());

                DishOrder dishOrder = new DishOrder(dishDAO.findById(dish.getDishIdentifier()), reference, dish.getQuantityOrdered(), List.of(dishOrderStatus));
                dishOrders.add(dishOrder);
            }
        }
        return dishOrderDAO.saveAll(dishOrders);
    }

    public Order updateDishOrderStatus(long orderId, long dishId, UpdateDishOrderStatus dishOrderStatus){
        Order order = orderDAO.findById(orderId);
        List<DishOrder> dishOrders = order.getDishOrders();

        DishOrder dishOrder = dishOrders.stream().filter(o -> o.getDish().getId() == dishId)
                .collect(Collectors.toCollection(ArrayList::new)).getFirst();

                DishOrderStatus dishOrderStatus1 = new DishOrderStatus(Instant.now(),dishOrderStatus.getStatus(),dishOrder.getId());

                dishOrderDAO.saveStatus(dishOrderStatus1);

        return orderDAO.findById(orderId);
    }

    public Order saveOrder(Order order) throws SQLException {
        return orderDAO.saveOrder(order);
    }
}
