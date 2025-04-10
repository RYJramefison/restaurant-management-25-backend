package school.hei.pingpongspring.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import school.hei.pingpongspring.model.DishOrder;
import school.hei.pingpongspring.model.Order;
import school.hei.pingpongspring.repository.dao.DishOrderDAO;
import school.hei.pingpongspring.repository.dao.OrderDAO;

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
}
