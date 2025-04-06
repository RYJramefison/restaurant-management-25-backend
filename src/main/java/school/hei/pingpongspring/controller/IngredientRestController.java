package school.hei.pingpongspring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import school.hei.pingpongspring.service.IngredientService;


@RestController
@RequiredArgsConstructor
public class IngredientRestController {
    private final IngredientService service;

    @GetMapping("/ingredients/{id}")
    public ResponseEntity<Object> findById(@PathVariable (name = "id", required = false) long id){
        return ResponseEntity.ok().body(service.findById(id));
    }
}
