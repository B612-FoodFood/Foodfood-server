package B612.foodfood.repository;

import B612.foodfood.domain.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface MealRepository extends JpaRepository<Meal,Long> {
    @Query("select m from Meal m " +
            "where m.date = :date")
    Optional<Meal> findByDate(@Param("date") LocalDateTime dateTime);
}
