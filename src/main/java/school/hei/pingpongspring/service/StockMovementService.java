package school.hei.pingpongspring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import school.hei.pingpongspring.model.StockMovement;
import school.hei.pingpongspring.repository.dao.StockMovementDAO;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockMovementService {
    private final StockMovementDAO stockMovementDAO;

    public List<StockMovement> getAll(){
        return stockMovementDAO.getAll();
    }
}
