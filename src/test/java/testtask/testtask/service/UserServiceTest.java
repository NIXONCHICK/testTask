package testtask.testtask.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import testtask.testtask.dto.UserDto;
import testtask.testtask.exception.ResourceNotFoundException;
import testtask.testtask.model.User;
import testtask.testtask.repository.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserDto userDto;

    @BeforeEach
    public void setup() {
        user = User.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .age(30)
                .weight(70.0)
                .height(175)
                .goal(User.Goal.WEIGHT_LOSS)
                .dailyCalorieNorm(2000)
                .build();

        userDto = UserDto.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .age(30)
                .weight(70.0)
                .height(175)
                .goal(User.Goal.WEIGHT_LOSS)
                .dailyCalorieNorm(2000)
                .build();
    }

    @Test
    public void whenCreateUser_thenReturnSavedUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDto savedUserDto = userService.createUser(userDto);

        assertThat(savedUserDto).isNotNull();
        assertThat(savedUserDto.getName()).isEqualTo(userDto.getName());
        assertThat(savedUserDto.getEmail()).isEqualTo(userDto.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void whenGetUserById_thenReturnUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserDto foundUserDto = userService.getUserById(1L);

        assertThat(foundUserDto).isNotNull();
        assertThat(foundUserDto.getId()).isEqualTo(user.getId());
        assertThat(foundUserDto.getName()).isEqualTo(user.getName());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    public void whenGetUserByNonExistingId_thenThrowException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(99L));

        verify(userRepository, times(1)).findById(99L);
    }

    @Test
    public void whenGetAllUsers_thenReturnUserList() {
        User user2 = User.builder()
                .id(2L)
                .name("Another User")
                .email("another@example.com")
                .age(25)
                .weight(60.0)
                .height(165)
                .goal(User.Goal.MAINTENANCE)
                .dailyCalorieNorm(1800)
                .build();

        when(userRepository.findAll()).thenReturn(Arrays.asList(user, user2));

        List<UserDto> userDtos = userService.getAllUsers();

        assertThat(userDtos).isNotNull();
        assertThat(userDtos.size()).isEqualTo(2);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void whenUpdateUser_thenReturnUpdatedUser() {
        UserDto updatedUserDto = UserDto.builder()
                .name("Updated Name")
                .email("updated@example.com")
                .age(35)
                .weight(75.0)
                .height(180)
                .goal(User.Goal.WEIGHT_GAIN)
                .build();

        User updatedUser = User.builder()
                .id(1L)
                .name("Updated Name")
                .email("updated@example.com")
                .age(35)
                .weight(75.0)
                .height(180)
                .goal(User.Goal.WEIGHT_GAIN)
                .dailyCalorieNorm(2500)
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        UserDto result = userService.updateUser(1L, updatedUserDto);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(updatedUserDto.getName());
        assertThat(result.getEmail()).isEqualTo(updatedUserDto.getEmail());
        assertThat(result.getAge()).isEqualTo(updatedUserDto.getAge());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void whenDeleteUser_thenRepositoryDeleteIsCalled() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(user);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    public void whenDeleteNonExistingUser_thenThrowException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUser(99L));

        verify(userRepository, times(1)).findById(99L);
        verify(userRepository, never()).delete(any(User.class));
    }
} 