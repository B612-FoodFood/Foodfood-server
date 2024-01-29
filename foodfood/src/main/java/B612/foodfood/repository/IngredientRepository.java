package B612.foodfood.repository;

import B612.foodfood.domain.Ingredient;
import B612.foodfood.service.IngredientService;
import io.swagger.models.auth.In;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface IngredientRepository extends JpaRepository<Ingredient,Long> {

    Optional<Ingredient> findByName(String name);
}
