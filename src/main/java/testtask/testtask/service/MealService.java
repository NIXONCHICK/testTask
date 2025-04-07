package testtask.testtask.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import testtask.testtask.dto.DishDto;
import testtask.testtask.dto.MealDto;
import testtask.testtask.exception.ResourceNotFoundException;
import testtask.testtask.model.Dish;
import testtask.testtask.model.Meal;
import testtask.testtask.model.User;
import testtask.testtask.repository.MealRepository;
import testtask.testtask.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MealService {

    private final MealRepository mealRepository;
    private final UserRepository userRepository;
    private final DishService dishService;

    @Autowired
    public MealService(MealRepository mealRepository, UserRepository userRepository, DishService dishService) {
        this.mealRepository = mealRepository;
        this.userRepository = userRepository;
        this.dishService = dishService;
    }

    public MealDto createMeal(MealDto mealDto) {
        User user = userRepository.findById(mealDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь", "id", mealDto.getUserId()));
        
        List<Dish> dishes = dishService.getDishesById(mealDto.getDishIds());
        
        Meal meal = new Meal();
        meal.setUser(user);
        
        if (mealDto.getDateTime() == null) {
            meal.setDateTime(LocalDateTime.now());
        } else {
            meal.setDateTime(mealDto.getDateTime());
        }
        
        meal.setMealType(mealDto.getMealType());
        meal.setDishes(dishes);
        
        Meal savedMeal = mealRepository.save(meal);
        return mapToDto(savedMeal);
    }

    public MealDto getMealById(Long id) {
        Meal meal = mealRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Прием пищи", "id", id));
        return mapToDto(meal);
    }

    public List<MealDto> getAllMealsByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь", "id", userId));
        
        List<Meal> meals = mealRepository.findByUserOrderByDateTimeDesc(user);
        return meals.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<MealDto> getMealsByUserIdAndDate(Long userId, LocalDate date) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь", "id", userId));
        
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        
        List<Meal> meals = mealRepository.findByUserAndDateBetween(user, startOfDay, endOfDay);
        return meals.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public Integer calculateTotalCaloriesForDay(Long userId, LocalDate date) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь", "id", userId));
        
        LocalDateTime startOfDay = date.atStartOfDay();
        
        Integer totalCalories = mealRepository.calculateTotalCaloriesForDay(user, startOfDay);
        return totalCalories != null ? totalCalories : 0;
    }

    public boolean isUserWithinCalorieNorm(Long userId, LocalDate date) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь", "id", userId));
        
        Integer totalCalories = calculateTotalCaloriesForDay(userId, date);
        return totalCalories <= user.getDailyCalorieNorm();
    }

    public MealDto updateMeal(Long id, MealDto mealDto) {
        Meal meal = mealRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Прием пищи", "id", id));
        
        if (!meal.getUser().getId().equals(mealDto.getUserId())) {
            User user = userRepository.findById(mealDto.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("Пользователь", "id", mealDto.getUserId()));
            meal.setUser(user);
        }
        
        List<Dish> dishes = dishService.getDishesById(mealDto.getDishIds());
        meal.setDishes(dishes);
        
        if (mealDto.getDateTime() != null) {
            meal.setDateTime(mealDto.getDateTime());
        }
        
        meal.setMealType(mealDto.getMealType());
        
        Meal updatedMeal = mealRepository.save(meal);
        return mapToDto(updatedMeal);
    }

    public void deleteMeal(Long id) {
        Meal meal = mealRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Прием пищи", "id", id));
        mealRepository.delete(meal);
    }

    private MealDto mapToDto(Meal meal) {
        List<DishDto> dishDtos = meal.getDishes().stream()
                .map(dish -> DishDto.builder()
                        .id(dish.getId())
                        .name(dish.getName())
                        .calories(dish.getCalories())
                        .proteins(dish.getProteins())
                        .fats(dish.getFats())
                        .carbohydrates(dish.getCarbohydrates())
                        .build())
                .collect(Collectors.toList());
        
        List<Long> dishIds = meal.getDishes().stream()
                .map(Dish::getId)
                .collect(Collectors.toList());
        
        return MealDto.builder()
                .id(meal.getId())
                .userId(meal.getUser().getId())
                .dateTime(meal.getDateTime())
                .mealType(meal.getMealType())
                .dishIds(dishIds)
                .dishes(dishDtos)
                .totalCalories(meal.getTotalCalories())
                .totalProteins(meal.getTotalProteins())
                .totalFats(meal.getTotalFats())
                .totalCarbohydrates(meal.getTotalCarbohydrates())
                .build();
    }
} 