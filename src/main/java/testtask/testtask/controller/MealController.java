package testtask.testtask.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import testtask.testtask.dto.MealDto;
import testtask.testtask.service.MealService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/meals")
public class MealController {

    private final MealService mealService;

    @Autowired
    public MealController(MealService mealService) {
        this.mealService = mealService;
    }

    @PostMapping
    public ResponseEntity<MealDto> createMeal(@Valid @RequestBody MealDto mealDto) {
        return new ResponseEntity<>(mealService.createMeal(mealDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MealDto> getMealById(@PathVariable Long id) {
        return ResponseEntity.ok(mealService.getMealById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<MealDto>> getAllMealsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(mealService.getAllMealsByUserId(userId));
    }

    @GetMapping("/user/{userId}/date/{date}")
    public ResponseEntity<List<MealDto>> getMealsByUserIdAndDate(
            @PathVariable Long userId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(mealService.getMealsByUserIdAndDate(userId, date));
    }

    @GetMapping("/user/{userId}/calories/date/{date}")
    public ResponseEntity<Integer> calculateTotalCaloriesForDay(
            @PathVariable Long userId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(mealService.calculateTotalCaloriesForDay(userId, date));
    }

    @GetMapping("/user/{userId}/within-norm/date/{date}")
    public ResponseEntity<Boolean> isUserWithinCalorieNorm(
            @PathVariable Long userId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(mealService.isUserWithinCalorieNorm(userId, date));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MealDto> updateMeal(@PathVariable Long id, @Valid @RequestBody MealDto mealDto) {
        return ResponseEntity.ok(mealService.updateMeal(id, mealDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMeal(@PathVariable Long id) {
        mealService.deleteMeal(id);
        return ResponseEntity.ok("Прием пищи успешно удален");
    }
} 