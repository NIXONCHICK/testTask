package testtask.testtask.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import testtask.testtask.dto.DishDto;
import testtask.testtask.exception.ResourceNotFoundException;
import testtask.testtask.model.Dish;
import testtask.testtask.repository.DishRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishService {

    private final DishRepository dishRepository;

    @Autowired
    public DishService(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    public DishDto createDish(DishDto dishDto) {
        Dish dish = mapToEntity(dishDto);
        Dish savedDish = dishRepository.save(dish);
        return mapToDto(savedDish);
    }

    public DishDto getDishById(Long id) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Блюдо", "id", id));
        return mapToDto(dish);
    }
    
    public List<Dish> getDishesById(List<Long> ids) {
        List<Dish> dishes = dishRepository.findAllById(ids);
        if (dishes.size() != ids.size()) {
            List<Long> foundIds = dishes.stream()
                    .map(Dish::getId)
                    .toList();
            
            ids.removeAll(foundIds);
            throw new ResourceNotFoundException("Некоторые блюда не найдены: " + ids);
        }
        return dishes;
    }

    public List<DishDto> getAllDishes() {
        List<Dish> dishes = dishRepository.findAll();
        return dishes.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<DishDto> searchDishesByName(String name) {
        List<Dish> dishes = dishRepository.findByNameContainingIgnoreCase(name);
        return dishes.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public DishDto updateDish(Long id, DishDto dishDto) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Блюдо", "id", id));

        dish.setName(dishDto.getName());
        dish.setCalories(dishDto.getCalories());
        dish.setProteins(dishDto.getProteins());
        dish.setFats(dishDto.getFats());
        dish.setCarbohydrates(dishDto.getCarbohydrates());

        Dish updatedDish = dishRepository.save(dish);
        return mapToDto(updatedDish);
    }

    public void deleteDish(Long id) {
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Блюдо", "id", id));
        dishRepository.delete(dish);
    }

    private Dish mapToEntity(DishDto dishDto) {
        Dish dish = new Dish();
        dish.setName(dishDto.getName());
        dish.setCalories(dishDto.getCalories());
        dish.setProteins(dishDto.getProteins());
        dish.setFats(dishDto.getFats());
        dish.setCarbohydrates(dishDto.getCarbohydrates());
        return dish;
    }

    private DishDto mapToDto(Dish dish) {
        return DishDto.builder()
                .id(dish.getId())
                .name(dish.getName())
                .calories(dish.getCalories())
                .proteins(dish.getProteins())
                .fats(dish.getFats())
                .carbohydrates(dish.getCarbohydrates())
                .build();
    }
} 