package testtask.testtask.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import testtask.testtask.model.Meal;
import testtask.testtask.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {
    
    List<Meal> findByUserOrderByDateTimeDesc(User user);

    @Query("SELECT m FROM Meal m WHERE m.user = :user AND m.dateTime BETWEEN :startDate AND :endDate ORDER BY m.dateTime")
    List<Meal> findByUserAndDateBetween(
            @Param("user") User user, 
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT SUM(d.calories) FROM Meal m JOIN m.dishes d WHERE m.user = :user AND DATE(m.dateTime) = DATE(:date)")
    Integer calculateTotalCaloriesForDay(@Param("user") User user, @Param("date") LocalDateTime date);
} 