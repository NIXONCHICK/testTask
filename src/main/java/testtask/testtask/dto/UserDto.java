package testtask.testtask.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import testtask.testtask.model.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private Long id;

    @NotBlank(message = "Имя не может быть пустым")
    @Size(min = 2, max = 50, message = "Имя должно содержать от 2 до 50 символов")
    private String name;

    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Email должен быть корректным")
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
    private User.Goal goal;
    
    private Integer dailyCalorieNorm;
} 