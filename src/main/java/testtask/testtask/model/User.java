package testtask.testtask.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Имя не может быть пустым")
    @Size(min = 2, max = 50, message = "Имя должно содержать от 2 до 50 символов")
    private String name;

    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Email должен быть корректным")
    @Column(unique = true)
    private String email;

    @NotNull(message = "Возраст не может быть пустым")
    @Min(value = 14, message = "Возраст должен быть не менее 14 лет")
    @Max(value = 120, message = "Возраст должен быть не более 120 лет")
    private Integer age;

    @NotNull(message = "Вес не может быть пустым")
    @DecimalMin(value = "30.0", message = "Вес должен быть не менее 30 кг")
    @DecimalMax(value = "300.0", message = "Вес должен быть не более 300 кг")
    private Double weight;

    @NotNull(message = "Рост не может быть пустым")
    @Min(value = 100, message = "Рост должен быть не менее 100 см")
    @Max(value = 250, message = "Рост должен быть не более 250 см")
    private Integer height;

    @NotNull(message = "Цель не может быть пустой")
    @Enumerated(EnumType.STRING)
    private Goal goal;

    @Column(name = "daily_calorie_norm")
    private Integer dailyCalorieNorm;

    public enum Goal {
        WEIGHT_LOSS,
        MAINTENANCE,
        WEIGHT_GAIN
    }

    @PrePersist
    @PreUpdate
    public void calculateDailyCalorieNorm() {
        double bmr;
        
        bmr = 267.978 + (11.322 * weight) + (3.949 * height) - (5.004 * age);
        
        // Коэффициенты активности (PAL):
        // 1.2 - минимальная активность
        // 1.375 - низкая активность
        // 1.55 - средняя активность
        // 1.725 - высокая активность
        // 1.9 - очень высокая активность

        // Используем средний уровень активности (1.55)
        double calorie = bmr * 1.55;
        
        // Коррекция в зависимости от цели
        switch (goal) {
            case WEIGHT_LOSS:
                calorie *= 0.8; // Уменьшаем калории на 20% для похудения
                break;
            case WEIGHT_GAIN:
                calorie *= 1.2; // Увеличиваем калории на 20% для набора массы
                break;
            case MAINTENANCE:
                break;
        }
        
        this.dailyCalorieNorm = (int) Math.round(calorie);
    }
} 