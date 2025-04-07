package testtask.testtask.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import testtask.testtask.dto.DailyReportDto;
import testtask.testtask.dto.MealDto;
import testtask.testtask.exception.ResourceNotFoundException;
import testtask.testtask.model.User;
import testtask.testtask.repository.UserRepository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    private final UserRepository userRepository;
    private final MealService mealService;

    @Autowired
    public ReportService(UserRepository userRepository, MealService mealService) {
        this.userRepository = userRepository;
        this.mealService = mealService;
    }

    public DailyReportDto generateDailyReport(Long userId, LocalDate date) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь", "id", userId));
        
        List<MealDto> meals = mealService.getMealsByUserIdAndDate(userId, date);
        Integer totalCalories = mealService.calculateTotalCaloriesForDay(userId, date);
        boolean withinCalorieNorm = mealService.isUserWithinCalorieNorm(userId, date);
        
        double totalProteins = 0.0;
        double totalFats = 0.0;
        double totalCarbohydrates = 0.0;
        
        for (MealDto meal : meals) {
            totalProteins += meal.getTotalProteins();
            totalFats += meal.getTotalFats();
            totalCarbohydrates += meal.getTotalCarbohydrates();
        }
        
        // Оставшееся количество калорий
        int caloriesRemaining = user.getDailyCalorieNorm() - totalCalories;
        if (caloriesRemaining < 0) {
            caloriesRemaining = 0;
        }
        
        return DailyReportDto.builder()
                .userId(userId)
                .userName(user.getName())
                .date(date)
                .dailyCalorieNorm(user.getDailyCalorieNorm())
                .totalCaloriesConsumed(totalCalories)
                .totalProteinsConsumed(totalProteins)
                .totalFatsConsumed(totalFats)
                .totalCarbohydratesConsumed(totalCarbohydrates)
                .withinCalorieNorm(withinCalorieNorm)
                .caloriesRemaining(caloriesRemaining)
                .meals(meals)
                .build();
    }
    
    public Map<LocalDate, DailyReportDto> generateMealHistoryReport(Long userId, LocalDate startDate, LocalDate endDate) {
      userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("Пользователь", "id", userId));

      Map<LocalDate, DailyReportDto> historyReport = new HashMap<>();
        
        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            DailyReportDto dailyReport = generateDailyReport(userId, currentDate);
            historyReport.put(currentDate, dailyReport);
            currentDate = currentDate.plusDays(1);
        }
        
        return historyReport;
    }
    
    public boolean checkCalorieNormCompliance(Long userId, LocalDate date) {
        return mealService.isUserWithinCalorieNorm(userId, date);
    }
}