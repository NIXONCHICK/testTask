package testtask.testtask.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import testtask.testtask.dto.UserDto;
import testtask.testtask.exception.ResourceNotFoundException;
import testtask.testtask.model.User;
import testtask.testtask.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto createUser(UserDto userDto) {
        User user = mapToEntity(userDto);
        user.calculateDailyCalorieNorm();
        User savedUser = userRepository.save(user);
        return mapToDto(savedUser);
    }

    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь", "id", id));
        return mapToDto(user);
    }

    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public UserDto updateUser(Long id, UserDto userDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь", "id", id));

        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setAge(userDto.getAge());
        user.setWeight(userDto.getWeight());
        user.setHeight(userDto.getHeight());
        user.setGoal(userDto.getGoal());
        
        user.calculateDailyCalorieNorm();
        User updatedUser = userRepository.save(user);
        return mapToDto(updatedUser);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь", "id", id));
        userRepository.delete(user);
    }

    private User mapToEntity(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setAge(userDto.getAge());
        user.setWeight(userDto.getWeight());
        user.setHeight(userDto.getHeight());
        user.setGoal(userDto.getGoal());
        return user;
    }

    private UserDto mapToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .age(user.getAge())
                .weight(user.getWeight())
                .height(user.getHeight())
                .goal(user.getGoal())
                .dailyCalorieNorm(user.getDailyCalorieNorm())
                .build();
    }
} 