package testtask.testtask.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "meals")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Пользователь не может быть пустым")
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull(message = "Дата и время не могут быть пустыми")
    @Column(name = "date_time")
    private LocalDateTime dateTime;

    @ManyToMany
    @JoinTable(
        name = "meal_dishes",
        joinColumns = @JoinColumn(name = "meal_id"),
        inverseJoinColumns = @JoinColumn(name = "dish_id")
    )
    private List<Dish> dishes = new ArrayList<>();
    
    @Enumerated(EnumType.STRING)
    @Column(name = "meal_type")
    private MealType mealType;
    
    public enum MealType {
        BREAKFAST, // Завтрак
        LUNCH,     // Обед
        DINNER,    // Ужин
        SNACK      // Перекус
    }
    
    // Метод для вычисления общего количества калорий в приеме пищи
    public int getTotalCalories() {
        return dishes.stream()
                .mapToInt(Dish::getCalories)
                .sum();
    }
    
    // Метод для вычисления общего количества белков в приеме пищи
    public double getTotalProteins() {
        return dishes.stream()
                .mapToDouble(Dish::getProteins)
                .sum();
    }
    
    // Метод для вычисления общего количества жиров в приеме пищи
    public double getTotalFats() {
        return dishes.stream()
                .mapToDouble(Dish::getFats)
                .sum();
    }
    
    // Метод для вычисления общего количества углеводов в приеме пищи
    public double getTotalCarbohydrates() {
        return dishes.stream()
                .mapToDouble(Dish::getCarbohydrates)
                .sum();
    }
} 