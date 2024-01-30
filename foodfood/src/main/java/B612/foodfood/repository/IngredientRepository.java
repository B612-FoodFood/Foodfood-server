package B612.foodfood.repository;

import B612.foodfood.domain.Category;
import B612.foodfood.domain.Ingredient;
import B612.foodfood.service.IngredientService;
import io.swagger.models.auth.In;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    Optional<Ingredient> findByName(String name);

    @Query("select i from Ingredient i " +
            "where i.name like concat('%',:keyword,'%')")
    List<Ingredient> findByKeyword(@Param("keyword") String keyword);

    @Query("select i from Ingredient i " +
            "where i.category = :category")
    List<Ingredient> findByCategory(@Param("category") Category category);
}
