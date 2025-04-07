package testtask.testtask.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import testtask.testtask.dto.DishDto;
import testtask.testtask.exception.ResourceNotFoundException;
import testtask.testtask.model.Dish;
import testtask.testtask.repository.DishRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DishServiceTest {

    @Mock
    private DishRepository dishRepository;

    @InjectMocks
    private DishService dishService;

    private Dish dish;
    private DishDto dishDto;

    @BeforeEach
    public void setup() {
        dish = Dish.builder()
                .id(1L)
                .name("Test Dish")
                .calories(300)
                .proteins(20.0)
                .fats(10.0)
                .carbohydrates(30.0)
                .build();

        dishDto = DishDto.builder()
                .id(1L)
                .name("Test Dish")
                .calories(300)
                .proteins(20.0)
                .fats(10.0)
                .carbohydrates(30.0)
                .build();
    }

    @Test
    public void whenCreateDish_thenReturnSavedDish() {
        when(dishRepository.save(any(Dish.class))).thenReturn(dish);

        DishDto savedDishDto = dishService.createDish(dishDto);

        assertThat(savedDishDto).isNotNull();
        assertThat(savedDishDto.getName()).isEqualTo(dishDto.getName());
        assertThat(savedDishDto.getCalories()).isEqualTo(dishDto.getCalories());
        verify(dishRepository, times(1)).save(any(Dish.class));
    }

    @Test
    public void whenGetDishById_thenReturnDish() {
        when(dishRepository.findById(1L)).thenReturn(Optional.of(dish));

        DishDto foundDishDto = dishService.getDishById(1L);

        assertThat(foundDishDto).isNotNull();
        assertThat(foundDishDto.getId()).isEqualTo(dish.getId());
        assertThat(foundDishDto.getName()).isEqualTo(dish.getName());
        verify(dishRepository, times(1)).findById(1L);
    }

    @Test
    public void whenGetDishByNonExistingId_thenThrowException() {
        when(dishRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> dishService.getDishById(99L));

        verify(dishRepository, times(1)).findById(99L);
    }

    @Test
    public void whenGetAllDishes_thenReturnDishList() {
        Dish dish2 = Dish.builder()
                .id(2L)
                .name("Another Dish")
                .calories(400)
                .proteins(25.0)
                .fats(15.0)
                .carbohydrates(35.0)
                .build();

        when(dishRepository.findAll()).thenReturn(Arrays.asList(dish, dish2));

        List<DishDto> dishDtos = dishService.getAllDishes();

        assertThat(dishDtos).isNotNull();
        assertThat(dishDtos.size()).isEqualTo(2);
        verify(dishRepository, times(1)).findAll();
    }

    @Test
    public void whenSearchDishesByName_thenReturnMatchingDishes() {
        when(dishRepository.findByNameContainingIgnoreCase("Test")).thenReturn(List.of(dish));

        List<DishDto> dishDtos = dishService.searchDishesByName("Test");

        assertThat(dishDtos).isNotNull();
        assertThat(dishDtos.size()).isEqualTo(1);
        assertThat(dishDtos.get(0).getName()).isEqualTo(dish.getName());
        verify(dishRepository, times(1)).findByNameContainingIgnoreCase("Test");
    }

    @Test
    public void whenUpdateDish_thenReturnUpdatedDish() {
        DishDto updatedDishDto = DishDto.builder()
                .name("Updated Dish")
                .calories(350)
                .proteins(22.0)
                .fats(12.0)
                .carbohydrates(32.0)
                .build();

        Dish updatedDish = Dish.builder()
                .id(1L)
                .name("Updated Dish")
                .calories(350)
                .proteins(22.0)
                .fats(12.0)
                .carbohydrates(32.0)
                .build();

        when(dishRepository.findById(1L)).thenReturn(Optional.of(dish));
        when(dishRepository.save(any(Dish.class))).thenReturn(updatedDish);

        DishDto result = dishService.updateDish(1L, updatedDishDto);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(updatedDishDto.getName());
        assertThat(result.getCalories()).isEqualTo(updatedDishDto.getCalories());
        verify(dishRepository, times(1)).findById(1L);
        verify(dishRepository, times(1)).save(any(Dish.class));
    }

    @Test
    public void whenDeleteDish_thenRepositoryDeleteIsCalled() {
        when(dishRepository.findById(1L)).thenReturn(Optional.of(dish));
        doNothing().when(dishRepository).delete(dish);

        dishService.deleteDish(1L);

        verify(dishRepository, times(1)).findById(1L);
        verify(dishRepository, times(1)).delete(dish);
    }

    @Test
    public void whenDeleteNonExistingDish_thenThrowException() {
        when(dishRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> dishService.deleteDish(99L));

        verify(dishRepository, times(1)).findById(99L);
        verify(dishRepository, never()).delete(any(Dish.class));
    }
} 