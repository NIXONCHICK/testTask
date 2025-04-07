package testtask.testtask.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import testtask.testtask.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MealDto {

    private Long id;

    @NotNull(message = "ID пользователя не может быть пустым")
    private Long userId;

    private LocalDateTime dateTime;

    @NotNull(message = "Тип приема пищи не может быть пустым")
    private Meal.MealType mealType;
    
    @NotEmpty(message = "Список блюд не может быть пустым")
    private List<Long> dishIds;
    
    private List<DishDto> dishes;
    private Integer totalCalories;
    private Double totalProteins;
    private Double totalFats;
    private Double totalCarbohydrates;
} 