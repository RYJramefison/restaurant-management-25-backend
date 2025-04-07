package school.hei.pingpongspring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import school.hei.pingpongspring.model.Ingredient;
import school.hei.pingpongspring.repository.dao.Criteria;
import school.hei.pingpongspring.repository.dao.IngredientDAO;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IngredientService {
    private final IngredientDAO subjectIngredient;

    public Ingredient findById(long id){
        return subjectIngredient.findById(id);
    }

    public List<Ingredient> findIngredientsByCriteria(List<Criteria> criteria , int size, int page){
        return subjectIngredient.findIngredientByCriteria(criteria , size, page);
    }
}
