package testtask.testtask.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "dishes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Dish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Название блюда не может быть пустым")
    @Size(min = 2, max = 100, message = "Название блюда должно содержать от 2 до 100 символов")
    private String name;

    @NotNull(message = "Количество калорий не может быть пустым")
    @Min(value = 0, message = "Количество калорий не может быть отрицательным")
    @Max(value = 5000, message = "Количество калорий должно быть не более 5000")
    private Integer calories;

    @NotNull(message = "Количество белков не может быть пустым")
    @DecimalMin(value = "0.0", message = "Количество белков не может быть отрицательным")
    @DecimalMax(value = "500.0", message = "Количество белков должно быть не более 500 г")
    private Double proteins;

    @NotNull(message = "Количество жиров не может быть пустым")
    @DecimalMin(value = "0.0", message = "Количество жиров не может быть отрицательным")
    @DecimalMax(value = "500.0", message = "Количество жиров должно быть не более 500 г")
    private Double fats;

    @NotNull(message = "Количество углеводов не может быть пустым")
    @DecimalMin(value = "0.0", message = "Количество углеводов не может быть отрицательным")
    @DecimalMax(value = "500.0", message = "Количество углеводов должно быть не более 500 г")
    private Double carbohydrates;
} 