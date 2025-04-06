package school.hei.pingpongspring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import school.hei.pingpongspring.model.Ingredient;
import school.hei.pingpongspring.repository.dao.IngredientDAO;

@Service
@RequiredArgsConstructor
public class IngredientService {
    private final IngredientDAO subjectIngredient;
    public Ingredient findById (long id){
        return subjectIngredient.findById(id);
    }
}
