package testtask.testtask.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyReportDto {

    private Long userId;
    private String userName;
    private LocalDate date;
    private Integer dailyCalorieNorm;
    private Integer totalCaloriesConsumed;
    private Double totalProteinsConsumed;
    private Double totalFatsConsumed;
    private Double totalCarbohydratesConsumed;
    private boolean withinCalorieNorm;
    private Integer caloriesRemaining;
    private List<MealDto> meals;
} 