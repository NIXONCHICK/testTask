package testtask.testtask.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import testtask.testtask.dto.DishDto;
import testtask.testtask.service.DishService;

import java.util.List;

@RestController
@RequestMapping("/api/dishes")
public class DishController {

    private final DishService dishService;

    @Autowired
    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    @PostMapping
    public ResponseEntity<DishDto> createDish(@Valid @RequestBody DishDto dishDto) {
        return new ResponseEntity<>(dishService.createDish(dishDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DishDto> getDishById(@PathVariable Long id) {
        return ResponseEntity.ok(dishService.getDishById(id));
    }

    @GetMapping
    public ResponseEntity<List<DishDto>> getAllDishes() {
        return ResponseEntity.ok(dishService.getAllDishes());
    }

    @GetMapping("/search")
    public ResponseEntity<List<DishDto>> searchDishesByName(@RequestParam String name) {
        return ResponseEntity.ok(dishService.searchDishesByName(name));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DishDto> updateDish(@PathVariable Long id, @Valid @RequestBody DishDto dishDto) {
        return ResponseEntity.ok(dishService.updateDish(id, dishDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDish(@PathVariable Long id) {
        dishService.deleteDish(id);
        return ResponseEntity.ok("Блюдо успешно удалено");
    }
} 