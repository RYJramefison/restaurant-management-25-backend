package school.hei.pingpongspring.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import school.hei.pingpongspring.model.Order;
import school.hei.pingpongspring.repository.dao.OrderDAO;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderDAO orderDAO;

    public Order findById(long id){
        return orderDAO.findById(id);
    }
}
