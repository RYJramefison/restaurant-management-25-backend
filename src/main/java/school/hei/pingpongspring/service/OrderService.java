package school.hei.pingpongspring.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import school.hei.pingpongspring.model.DishOrder;
import school.hei.pingpongspring.model.DishOrderStatus;
import school.hei.pingpongspring.model.Order;
import school.hei.pingpongspring.repository.dao.DishOrderDAO;
import school.hei.pingpongspring.repository.dao.OrderDAO;

import java.sql.SQLException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderDAO orderDAO;
    private final DishOrderDAO dishOrderDAO;

    public Order findById(long id){
        return orderDAO.findById(id);
    }

    public List<DishOrder> getdishByOrder(long id){
        return orderDAO.getDishByOrder(id);
    }

    public List<DishOrder> updateDishOrder(List<DishOrder>  dishOrderList) throws Exception {
        return dishOrderDAO.saveAll(dishOrderList);
    }

    public Order updateDishOrderStatus(long orderId, long dishId, DishOrderStatus dishOrderStatus){
        List<DishOrder> dishOrders = dishOrderDAO.findDishOrderByDish(dishId);

        dishOrders.stream().forEach(dishOrder -> {
            if (dishOrder.getOrderId() == orderId){
                dishOrderDAO.saveStatus(dishOrderStatus);
            }
        });

        return orderDAO.findById(orderId);
    }

    public Order saveOrder(Order order) throws SQLException {
        return orderDAO.saveOrder(order);
    }
}
