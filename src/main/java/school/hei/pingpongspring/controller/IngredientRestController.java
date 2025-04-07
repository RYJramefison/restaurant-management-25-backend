package school.hei.pingpongspring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.hei.pingpongspring.model.Ingredient;
import school.hei.pingpongspring.repository.dao.Criteria;
import school.hei.pingpongspring.service.IngredientService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/ingredients")
public class IngredientRestController {
    private final IngredientService service;

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable (name = "id", required = false) long id){
        return ResponseEntity.ok().body(service.findById(id));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Ingredient>> findByCriteria(
            @RequestParam(required = false) String company,
            @RequestParam(required = false) LocalDateTime dateEvent,
            @RequestParam(required = false) LocalDateTime dateEventMin,
            @RequestParam(required = false) LocalDateTime dateEventMax,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String location
    ){
        List<Criteria> criteria = new ArrayList<>();
        return null;
    }
}
